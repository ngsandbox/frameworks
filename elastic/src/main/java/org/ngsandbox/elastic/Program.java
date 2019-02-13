package org.ngsandbox.elastic;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.ngsandbox.elastic.entities.ElasticJson;
import org.ngsandbox.elastic.entities.Price;
import org.ngsandbox.elastic.entities.Quote;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

public class Program {

    private static final Random random = new Random();
    private static final String USD = "USD/RUB";
    private static final Map<String, BigDecimal> data = new HashMap<>();
    private static final Map<String, BigDecimal> usdCoeffs = new HashMap<>();


    static {
        data.put(USD, BigDecimal.valueOf(50d));
        data.put("AUD/RUB", BigDecimal.valueOf(40d));
        data.put("EUR/RUB", BigDecimal.valueOf(60d));
        data.put("GBP/RUB", BigDecimal.valueOf(70d));

        usdCoeffs.put("tp1", BigDecimal.valueOf(1.02));
        usdCoeffs.put("tp2", BigDecimal.valueOf(1.04));
        usdCoeffs.put("tp3", BigDecimal.valueOf(1.08));
        usdCoeffs.put("tp4", BigDecimal.valueOf(1.16));
        usdCoeffs.put("tp5", BigDecimal.valueOf(1.31));
    }

    public static void main(String... args) throws Exception {

        try (TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))) {
            long ticks = 0;
            while (ticks < 1000) {
                Map<String, BigDecimal> updatedPrices = new HashMap<>();
                long finalTicks = ticks;
                data.forEach((symbol, price) -> publishQuotes(client, updatedPrices, finalTicks, symbol, price));
                data.clear();
                data.putAll(updatedPrices);

                ticks++;
                Thread.sleep(2000);
            }
        }
    }

    private static void publishQuotes(TransportClient client, Map<String, BigDecimal> updatedPrices, long finalTicks, String symbol, BigDecimal price) {
        Quote baseQuote = createQuote(symbol, price.doubleValue());
        publishEntity(client, "base-quote", baseQuote);
        List<Quote> calcQuotes = createCalcQuotes(baseQuote);
        if (calcQuotes != null) {
            calcQuotes.forEach(calcQuote -> {
                publishEntity(client, "calc-quote", calcQuote);
                if (finalTicks % 15 == 0) {
                    publishEntity(client, "publish-quote", calcQuote);
                }
            });
        }
        updatedPrices.put(symbol, baseQuote.getPrices().stream()
                .filter(p -> p.getVolume() == 0).map(p -> BigDecimal.valueOf(p.getBid())).findFirst().get());
    }

    private static List<Quote> createCalcQuotes(Quote baseQuote) {
        if (!baseQuote.getSymbol().equals(USD)) return null;
        List<Quote> calcQuotes = new ArrayList<>();
        usdCoeffs.forEach((tier, coeff) -> {
            double c = coeff.doubleValue();
            List<Price> prices = baseQuote.getPrices().stream()
                    .map(p -> Price.builder()
                            .volume(p.getVolume())
                            .bid(p.getBid() * c)
                            .offer(p.getOffer() * (2 - c)).build())
                    .collect(Collectors.toList());
            Quote calcQuote = Quote.builder()
                    .id(UUID.randomUUID().toString())
                    .created(new Date())
                    .symbol(baseQuote.getSymbol())
                    .tier(tier)
                    .prices(prices)
                    .build();
            calcQuotes.add(calcQuote);
        });

        return calcQuotes;
    }

    private static <T extends ElasticJson> void publishEntity(TransportClient client, String indexName, T entity) {
        String str = entity.takeJson();
        if (str == null)
            return;
        //BulkRequestBuilder bulkRequestBuilder = BulkAction.INSTANCE.newRequestBuilder(client);
        client.prepareIndex(indexName, "quotes")
                .setId(entity.takeId())
                .setSource(str, XContentType.JSON)
                .setOpType(DocWriteRequest.OpType.INDEX)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL)
                .setTimeout(TimeValue.timeValueMillis(100))
                .execute(new ActionListener<IndexResponse>() {
                    @Override
                    public void onResponse(IndexResponse response) {
                        System.out.println("Index " + indexName + " result: " + response.getResult() + " data: " + str);
                    }

                    @Override
                    public void onFailure(Exception ex) {
                        System.out.println("The document for index " + indexName + " has been not been indexed " + ex);
                    }
                });
    }

    private static Quote createQuote(String symbol, double bid) {
        double coeff = 0.001;
        double spread;
        spread = random.nextBoolean() ? bid * coeff : bid * coeff * -1;
        bid += spread;
        return Quote.builder().symbol(symbol).tier("base")
                .created(new Date()).id(UUID.randomUUID().toString())
                .prices(Arrays.asList(
                        Price.builder().volume(0).bid(bid * 1.0).offer(bid * 0.9).build(),
                        Price.builder().volume(10000).bid(bid * 1.04).offer(bid * 0.92).build(),
                        Price.builder().volume(100_0000).bid(bid * 1.06).offer(bid * 0.94).build()))
                .build();
    }
}
