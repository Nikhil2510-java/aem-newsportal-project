package com.bhasaka.newsportal.core.services;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;


@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {"process.label=NP Content Activation Process"}
)
public class NPActivationProcess implements WorkflowProcess {
    @Reference
    Replicator replicator;
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        Session session=workflowSession.adaptTo(Session.class);
        String payload=workItem.getWorkflowData().getPayload().toString();
        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE,payload);
        } catch (ReplicationException e) {
            throw new RuntimeException(e);
        }
    }
}
