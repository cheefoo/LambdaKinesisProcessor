package com.tayo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;

/**
 * Created by temitayo on 8/30/17.
 */
public class DDBLoaderTest
{
    static AmazonDynamoDB clientBuilder;
    public static void main(String[] args)
    {
        clientBuilder = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
        DynamoDBMapper mapper = new DynamoDBMapper(clientBuilder);

        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression().withHashKeyValues(new CurrencyPrice("NGN"));
        QueryResultPage<CurrencyPrice> results = mapper.queryPage(CurrencyPrice.class, queryExpression);
        for(int i = 0; i < results.getResults().size(); i++)
        {
            System.out.println("DDB Results : " + results.getResults().get(i).getCurrencySellRate());
        }

    }
}
