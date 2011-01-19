package org.nuxeo.ecm.platform.exalead.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DataModel;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.impl.LifeCycleFilter;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.platform.api.ws.DocumentBlob;
import org.nuxeo.ecm.platform.api.ws.DocumentDescriptor;
import org.nuxeo.ecm.platform.api.ws.session.WSRemotingSession;
import org.nuxeo.ecm.platform.exalead.ws.api.WSExalead;
import org.nuxeo.ecm.platform.indexing.gateway.ws.UUIDPage;
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

    @Override
    @WebMethod
    public UUIDPage getRecursiveChildrenUUIDsByPage(@WebParam(name = "sessionId") String sid, @WebParam(name = "uuid") String uuid , @WebParam(name = "page") int page, @WebParam(name = "pageSize") int pageSize) throws ClientException {

        CoreSession session = initSession(sid).getDocumentManager();

        List<String> uuids = new ArrayList<String>();
        IdRef parentRef = new IdRef(uuid);
        DocumentModel parent = session.getDocument(parentRef);
        String path = parent.getPathAsString();

        String query = "select ecm:uuid from Document where ecm:path startswith '" + path + " AND ecm:currentLifeCycleState != 'deleted' order by ecm:uuid";

        IterableQueryResult result = session.queryAndFetch(query, "NXQL");
        boolean hasMore = false;
        try {
            if (page>1) {
                int skip=(page-1)*pageSize;
                result.skipTo(skip);
            }

            for (Map<String, Serializable> record:result) {
                uuids.add((String)record.get(NXQL.ECM_UUID));
                if (uuids.size()==pageSize) {
                    hasMore=true;
                    break;
                }
            }
        }
        finally {
            result.close();
        }
        return new UUIDPage(uuids.toArray(new String[uuids.size()]),page,hasMore) ;
    }


    @Override
    @WebMethod
    public String[] getRecursiveChildrenUUIDs(@WebParam(name = "sessionId") String sid, @WebParam(name = "uuid") String uuid ) throws ClientException {

        CoreSession session = initSession(sid).getDocumentManager();

        List<String> uuids = new ArrayList<String>();
        IdRef parentRef = new IdRef(uuid);
        DocumentModel parent = session.getDocument(parentRef);
        String path = parent.getPathAsString();

        String query = "select ecm:uuid from Document where ecm:path startswith '" + path + "'  AND ecm:currentLifeCycleState != 'deleted' order by ecm:uuid";

        IterableQueryResult result = session.queryAndFetch(query, "NXQL");

        try {
            for (Map<String, Serializable> record:result) {
                uuids.add((String)record.get(NXQL.ECM_UUID));
            }
        }
        finally {
            result.close();
        }

        return uuids.toArray(new String[uuids.size()]);
    }

}
