package org.ngsandbox.elastic;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.ngsandbox.elastic.entities.ElasticJson;
import org.ngsandbox.elastic.entities.Price;
import org.ngsandbox.elastic.entities.Quote;
import org.ngsandbox.elastic.entities.QuoteIndex;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ElasticApp implements AutoCloseable {

    private final TransportClient client;
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

    private ElasticApp(String host, int port) throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
    }

    private void start(int ticksCount, int timeoutMs) throws Exception {
        long ticks = 0;
        while (ticks < ticksCount) {
            Map<String, BigDecimal> updatedPrices = new HashMap<>();
            long finalTicks = ticks;
            data.forEach((symbol, price) -> {
                Quote baseQuote = createQuote(symbol, price.doubleValue());
                List<QuoteIndex> indices = convertToIndices(Stream.of(baseQuote));
                publishEntities("base-quote", indices);
                List<Quote> calcQuotes = createCalcQuotes(baseQuote);
                if (calcQuotes != null) {
                    publishEntities("calc-quote", convertToIndices(calcQuotes.stream()));
                    if (finalTicks % 15 == 0) {
                        publishEntities("publish-quote", convertToIndices(calcQuotes.stream()));
                    }
                }
                updatedPrices.put(symbol, baseQuote.getPrices().stream()
                        .filter(p -> p.getVolume() == 0).map(p -> BigDecimal.valueOf(p.getBid())).findFirst().get());
            });
            data.clear();
            data.putAll(updatedPrices);

            ticks++;
            Thread.sleep(timeoutMs);
        }
    }

    private List<QuoteIndex> convertToIndices(Stream<Quote> quotes) {
        return quotes.flatMap(q -> q.getPrices().stream().map(p -> new QuoteIndex(q, p))).collect(Collectors.toList());
    }

    public static void main(String... args) throws Exception {
        try (ElasticApp app = new ElasticApp("localhost", 9300)) {
            app.start(1000, 5000);
        }
    }

    private static List<Quote> createCalcQuotes(Quote baseQuote) {
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

    private <T extends ElasticJson> void publishEntities(String indexName, List<T> entities) {
        log.debug("Send to index {}", indexName);
        log.trace("Send to index {} entities {}", indexName, entities);
        BulkRequestBuilder builder = getBulkRequest(indexName, entities);
        BulkResponse bulkResponse = builder.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            log.error("Error to insert index {}. Msg {}. Responses {}", indexName, bulkResponse.buildFailureMessage(), bulkResponse.getItems());
        } else {
            log.debug("Successfully sent to index {}", indexName);
        }
    }

    private <T extends ElasticJson> BulkRequestBuilder getBulkRequest(String indexName, List<T> entities) {

        BulkRequestBuilder builder = client.prepareBulk();
        for (T entity : entities) {
            String json = entity.takeJson();
            if (json == null) continue;
            builder.add(client.prepareIndex(indexName, "quotes")
                    .setId(entity.takeId())
                    .setSource(json, XContentType.JSON)
                    .setOpType(DocWriteRequest.OpType.INDEX));
        }
        return builder;
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

    @Override
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
