package fr.jenkins.plugins.mac.provision

import java.util.logging.Logger

import javax.annotation.CheckForNull

import fr.jenkins.plugins.mac.slave.MacSlave
import hudson.Extension
import hudson.model.Computer
import hudson.model.Label
import hudson.model.Node

@Extension
class MacInProvisioning extends InProvisioning {
    private static final Logger LOGGER = Logger.getLogger(MacInProvisioning.name);

    private static boolean isNotAcceptingTasks(Node n) {
        Computer computer = n.toComputer();
        return computer != null && (computer.isLaunchSupported() // Launcher hasn't been called yet
                || !n.isAcceptingTasks()) // node is not ready yet
        ;
    }

    @Override
    public Set<String> getInProvisioning(@CheckForNull Label label) {
        if (label != null) {
            return label.getNodes().stream()
                    .filter { node ->
                        node instanceof MacSlave
                    }
                    .filter { node ->
                        MacInProvisioning.isNotAcceptingTasks(node)
                    }
                    .collect([] as HashSet){it.name}
        } else {
            return Collections.emptySet();
        }
    }
}
