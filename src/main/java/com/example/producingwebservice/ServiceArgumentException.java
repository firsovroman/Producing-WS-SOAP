package com.example.producingwebservice;

import io.spring.guides.gs_producing_web_service.Myfault;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER)
public class ServiceArgumentException extends Exception{

    Myfault myfault;

    public ServiceArgumentException(Myfault myfault) {
        super(myfault.getDescription());
        this.myfault = myfault;
    }

    public ServiceArgumentException(String message, Throwable cause, Myfault myfault) {
        super(message, cause);
        this.myfault = myfault;
    }

    public ServiceArgumentException(Throwable cause, Myfault myfault) {
        super(cause);
        this.myfault = myfault;
    }

    public ServiceArgumentException(Throwable cause, boolean enableSuppression, boolean writableStackTrace, Myfault myfault) {
        super(myfault.getDescription(), cause, enableSuppression, writableStackTrace);
        this.myfault = myfault;
    }

    public Myfault getMyfault() {
        return myfault;
    }

    public void setMyfault(Myfault myfault) {
        this.myfault = myfault;
    }
}
