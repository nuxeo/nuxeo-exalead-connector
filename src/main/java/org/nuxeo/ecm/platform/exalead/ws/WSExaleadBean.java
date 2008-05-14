package org.nuxeo.ecm.platform.exalead.ws;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.jboss.annotation.ejb.SerializedConcurrentAccess;
import org.nuxeo.ecm.platform.exalead.ws.api.WSExalead;
import org.nuxeo.ecm.platform.indexing.gateway.ws.WSIndexingGatewayBean;

@Stateless
@SerializedConcurrentAccess
@WebService(name = "WSExaleadInterface", serviceName = "WSExaleadService")
@SOAPBinding(style = Style.DOCUMENT)
public class WSExaleadBean extends WSIndexingGatewayBean implements WSExalead {

    /**
     *
     */
    private static final long serialVersionUID = 87687687681L;


}
