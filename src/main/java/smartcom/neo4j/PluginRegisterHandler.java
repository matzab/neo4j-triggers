package smartcom.neo4j;

import org.neo4j.annotations.service.ServiceProvider;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.kernel.extension.ExtensionFactory;
import org.neo4j.kernel.extension.ExtensionType;
import org.neo4j.kernel.extension.context.ExtensionContext;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;
import org.neo4j.logging.Log;
import org.neo4j.logging.internal.LogService;


/**
 *This class takes care of lifecycle and manages
 * the dependencies passed to the plugin
 */
@ServiceProvider
public class PluginRegisterHandler extends ExtensionFactory<PluginRegisterHandler.Dependencies> {
    public PluginRegisterHandler() {
        super(ExtensionType.DATABASE, "pluginRegisterHandler");
    }

    @Override
    public Lifecycle newInstance(ExtensionContext extensionContext, final Dependencies dependencies) {
        return new LifecycleAdapter() {
            final LogService logService = dependencies.log();
            final Log log = logService.getUserLog(PluginRegisterHandler.class);
            Neo4jTriggersPlugin triggersPlugin;

            @Override
            public void start() {
                System.out.println("Trigger Plugin starting...");
                triggersPlugin = new Neo4jTriggersPlugin(log);
                dependencies.getGraphDatabaseService().registerTransactionEventListener("neo4j", triggersPlugin);
            }

            @Override
            public void shutdown() {
                System.out.println("Trigger Plugin stopping");
                dependencies.getGraphDatabaseService().unregisterTransactionEventListener("neo4j", triggersPlugin);
            }
        };
    }

    interface Dependencies {
        DatabaseManagementService getGraphDatabaseService();

        LogService log();
    }
}
