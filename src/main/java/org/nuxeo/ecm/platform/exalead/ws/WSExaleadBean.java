package org.nuxeo.ecm.platform.exalead.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DataModel;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.LifeCycleFilter;
import org.nuxeo.ecm.platform.api.ws.DocumentBlob;
import org.nuxeo.ecm.platform.api.ws.DocumentDescriptor;
import org.nuxeo.ecm.platform.api.ws.session.WSRemotingSession;
import org.nuxeo.ecm.platform.exalead.ws.api.WSExalead;
import org.nuxeo.ecm.platform.indexing.gateway.ws.WSIndexingGatewayBean;

@WebService(name = "WSExaleadInterface", serviceName = "WSExaleadService")
@SOAPBinding(style = Style.DOCUMENT)
public class WSExaleadBean extends WSIndexingGatewayBean implements WSExalead {

    /**
     *
     */
    private static final long serialVersionUID = 87687687681L;


    @Override
    @WebMethod
    public DocumentDescriptor[] getChildren(
            @WebParam(name = "sessionId") String sessionId,
            @WebParam(name = "uuid") String uuid) throws ClientException {
        WSRemotingSession rs = initSession(sessionId);
        LifeCycleFilter filter = new LifeCycleFilter("deleted", false);
        DocumentModelList docList = rs.getDocumentManager().getChildren(new IdRef(uuid), null, null, filter,null);
        DocumentDescriptor[] docs = new DocumentDescriptor[docList.size()];
        int i = 0;
        for (DocumentModel doc : docList) {
            docs[i++] = new DocumentDescriptor(doc);
        }
        return docs;
    }

}
