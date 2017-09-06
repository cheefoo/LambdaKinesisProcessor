package com.tayo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by temitayo on 8/29/17.
 * **
 * Install library
 * mvn install:install-file -Dfile=/Users/temitayo/workspace/kinesis-aggregation/java/KinesisDeaggregator/dist/KinesisDeaggregator-1.0.jar
 * -DgroupId=com.amazonaws.services.kinesis
 * -DartifactId=kinesis-aggregation-java -Dversion=1.0.1 -Dpackaging=jar
 */

public class KinesisProcessor
{
    static AmazonDynamoDB clientBuilder;
    final static CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
    public static void handleRequest(KinesisEvent event, Context context)
    {
        context.getLogger().log("Input is " + event);
        //context.getLogger().log("Records Obtained " + event.getRecords().get(0).getKinesis().getData().toString());
        clientBuilder = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
        DynamoDBMapper mapper = new DynamoDBMapper(clientBuilder);
        String retrievedRecord = null;
        try
        {
            retrievedRecord = decoder.decode(event.getRecords().get(0).getKinesis().getData()).toString();
            System.out.println("Retrieved Record is : " + retrievedRecord) ;
        }
        catch (CharacterCodingException e)
        {
            System.out.println(e.getMessage());
        }

        //token to lookup
        String [] tokens = retrievedRecord.split(",");
        String ccySymbol = tokens[1].trim();
        System.out.println("Symbol is : " + ccySymbol);
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression().withHashKeyValues(new CurrencyPrice(ccySymbol));

        QueryResultPage<CurrencyPrice> results = mapper.queryPage(CurrencyPrice.class, queryExpression);

        for(int i = 0; i < results.getResults().size(); i++)
        {
            System.out.println("DDB Results : " + results.getResults().get(i).toString());
        }


        double profitMadeOnTxn = 0.0d;
        if(results.getResults().size() > 0)
        {
            if(tokens[2].equalsIgnoreCase("SELL"))
            {
                profitMadeOnTxn =  Double.valueOf(tokens[3]) - Double.valueOf(results.getResults().get(0).getCurrencySellRate());
            }
            else
            {
                profitMadeOnTxn =  Double.valueOf(tokens[3]) - Double.valueOf(results.getResults().get(0).getCuurencyBuyRate());
            }
        }
        else
        {
            System.out.println("unable to retrieve results from DDB");
        }



         System.out.println("We made a profit of : " + profitMadeOnTxn);




    }

}
