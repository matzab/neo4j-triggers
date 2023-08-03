package smartcom.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventListenerAdapter;
import org.neo4j.logging.Log;

/**
 * This class contains trigger for logging database changes
 *
 * TODO: implement logging errors and roll backs
 */
public class Neo4jTriggersPlugin extends TransactionEventListenerAdapter<Object> {
    public Log log;
    StringBuilder stringBuilder;
    long startTime;
    String query;
    public Neo4jTriggersPlugin(Log log) {
        this.log = log;
        System.out.println("Plugin initialized");
    }

    @Override
    public void afterCommit(TransactionData transactionData, Object o, GraphDatabaseService graphDatabaseService) {
        stringBuilder = new StringBuilder();
        TransactionDataLogFormatter.formatData(transactionData, stringBuilder);
        log.info(stringBuilder.toString());
    }

    @Override
    public Object beforeCommit(TransactionData data, Transaction transaction, GraphDatabaseService databaseService) throws Exception {
        return null;
    }
    @Override
    public void afterRollback(TransactionData data, Object state, GraphDatabaseService databaseService) {
    }

}