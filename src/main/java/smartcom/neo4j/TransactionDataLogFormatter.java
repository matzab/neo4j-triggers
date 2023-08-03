package smartcom.neo4j;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.TransactionData;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains logic to format logs
 *
 * TODO: add more properties to collect data from, add other formats json xml etc.
 */
public class TransactionDataLogFormatter {

    public static void formatData(TransactionData transactionData, StringBuilder stringBuilder){
        stringBuilder.append(" Transaction committed: ");
        stringBuilder.append("\n-----------------------");
        stringBuilder.append("\nTransaction ID: ");
        stringBuilder.append(transactionData.getTransactionId());
        stringBuilder.append("\nTransaction time: ");
        stringBuilder.append(formatDate(transactionData.getCommitTime()));
        if(transactionData.createdNodes().iterator().hasNext()) {
            stringBuilder.append("\nCreated node(s) IDs: ");
            for(Node node: transactionData.createdNodes()){
                stringBuilder.append( node.getElementId());
                stringBuilder.append(", ");
            }
        }
        if(transactionData.deletedNodes().iterator().hasNext()) {
            stringBuilder.append("\nDeleted node(s) IDs: ");
            for(Node node: transactionData.deletedNodes()){
                stringBuilder.append( node.getElementId());
                stringBuilder.append(", ");
            }
        }
        if(transactionData.createdRelationships().iterator().hasNext()) {
            stringBuilder.append("\nRelationships Created:");
            for (Relationship createdRelationship : transactionData.createdRelationships()) {
                stringBuilder.append("\nRelationship ID: " + createdRelationship.getElementId());
                stringBuilder.append("\nType: " + createdRelationship.getType().name());
                stringBuilder.append("\nStart Node ID: " + createdRelationship.getStartNode().getElementId());
                stringBuilder.append("\nEnd Node ID: " + createdRelationship.getEndNode().getElementId());
            }
        }
        if(transactionData.deletedRelationships().iterator().hasNext()) {
            stringBuilder.append("\nRelationships Deleted:");
            for (Relationship deletedRelationship : transactionData.deletedRelationships()) {
                stringBuilder.append("\nRelationship ID: " + deletedRelationship.getElementId());
                stringBuilder.append("\nType: " + deletedRelationship.getType().name());
                stringBuilder.append("\nStart Node ID: " + deletedRelationship.getStartNode().getElementId());
                stringBuilder.append("\nEnd Node ID: " + deletedRelationship.getEndNode().getElementId());
            }
        }
        stringBuilder.append("\n-----------------------");
    }
     private static String formatDate(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
}
