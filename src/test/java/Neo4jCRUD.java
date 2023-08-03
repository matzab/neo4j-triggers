import org.neo4j.driver.Record;
import org.neo4j.driver.*;


/**
 * Mock class to test neo4j database graph operations with cypher queries
 *
 * TODO: expend on complexity of queries -> move to JUit5 tests
 */
public class Neo4jCRUD {
    public static void main(String[] args) {
        String uri = "bolt://localhost:7687";
        String username = "neo4j";
        String password = "my_password";

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
             Session session = driver.session()) {
            getNode(session);
            // Create a node
            createNode(session, "Person", "Johnny");

            // Read nodes
            readNodes(session, "Person");

            // Update a node
            updateNode(session, "Person", "Johnny", "New Name");

            // Read nodes again to verify the update
            readNodes(session, "Person");

            // Delete the node
            deleteNode(session, "Person", "New Name");
        }
    }

    private static void getNode(Session session) {
        String query = "MATCH (p:Person)-[:KNOWS]->() RETURN p.name";
        Result result = session.run(query);
        while (result.hasNext()) {
            Record record = result.next();
            String name = record.get("name").asString();
            System.out.println(name);
        }
    }

    private static void createNode(Session session, String label, String name) {
        String query = "CREATE (n:" + label + " {name: $name})";
        session.run(query, Values.parameters("name", name));
        System.out.println("Node created: " + name);
    }

    private static void readNodes(Session session, String label) {
        String query = "MATCH (n:" + label + ") RETURN n.name AS name";
        Result result = session.run(query);
        System.out.println("Nodes of type " + label + ":");
        while (result.hasNext()) {
            Record record = result.next();
            String name = record.get("name").asString();
            System.out.println(name);
        }
    }

    private static void updateNode(Session session, String label, String oldName, String newName) {
        String query = "MATCH (n:" + label + " {name: $oldName}) SET n.name = $newName";
        session.run(query, Values.parameters("oldName", oldName, "newName", newName));
        System.out.println("Node updated: " + oldName + " -> " + newName);
    }

    private static void deleteNode(Session session, String label, String name) {
        String query = "MATCH (n:" + label + " {name: $name}) DELETE n";
        session.run(query, Values.parameters("name", name));
        System.out.println("Node deleted: " + name);
    }
}
