package org.nuxeo.ecm.platform.exalead.ws;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.platform.api.ws.DocumentBlob;
import org.nuxeo.ecm.platform.indexing.gateway.adapter.BaseIndexingAdapter;
import org.nuxeo.ecm.platform.indexing.gateway.adapter.IndexingAdapter;

public class NoteBlobAdapter extends BaseIndexingAdapter implements IndexingAdapter {

    protected static final Log log = LogFactory.getLog(NoteBlobAdapter.class);

    @Override
    public DocumentBlob[] adaptDocumentBlobs(CoreSession session, String uuid,
            DocumentBlob[] blobs) throws ClientException {

        DocumentModel doc = session.getDocument(new IdRef(uuid));
        if ("Note".equals(doc.getType())) {

            BlobHolder bh = doc.getAdapter(BlobHolder.class);
            if (bh!=null && bh.getBlob()!=null) {
                try {
                    String url = "/nxbigfile/" + doc.getRepositoryName() + "/" + uuid + "/blobholder:0/" + bh.getBlob().getFilename();
                    //DocumentBlob db = new DocumentBlob(bh.getBlob().getFilename(), bh.getBlob());
                    Blob blob = bh.getBlob();
                    DocumentBlob db = new DocumentBlob(blob.getFilename(),blob.getEncoding(), blob.getMimeType(), url);
                    List<DocumentBlob> dbs = new ArrayList<DocumentBlob>();
                    dbs.add(db);
                    if (blobs!=null) {
                        for (DocumentBlob dbi : blobs) {
                            dbs.add(dbi);
                        }
                    }
                    return dbs.toArray(new DocumentBlob[dbs.size()]);
                }
                catch (Exception e) {
                }
            }
        }
        return blobs;
    }
}
