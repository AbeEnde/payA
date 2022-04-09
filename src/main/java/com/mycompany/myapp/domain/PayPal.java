package com.mycompany.myapp.domain;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.PayPalException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentResponseDetailsType;
import urn.ebay.apis.eBLBaseComponents.GetExpressCheckoutDetailsResponseDetailsType;
import urn.ebay.apis.eBLBaseComponents.PayerInfoType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.PaymentInfoType;
import urn.ebay.apis.eBLBaseComponents.PaymentStatusCodeType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

public class PayPal {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(PayPal.class);

    PayPalProperty pro = new PayPalProperty();
    String tokn;
    PaymentActionCodeType paymentAction;
    List<PaymentDetailsType> pda;
    final Map<String, String> serviceConfigurationProperties = pro.getAcctAndConfig();
    final PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(serviceConfigurationProperties);

    public String setExpressCheckout(
        Long payerId,
        String paymentAmount,
        urn.ebay.apis.eBLBaseComponents.CurrencyCodeType currencyCodeType,
        String returnURL,
        String cancelURL,
        PaymentActionCodeType paymentAction
    )
        throws PayPalException, SSLConfigurationException, InvalidCredentialException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, IOException, ParserConfigurationException, SAXException {
        Map<String, String> sdkConfig = new HashMap<String, String>();
        sdkConfig.put("mode", "sandbox");
        sdkConfig.put("acct1.UserName", "sb-v3z43o8598034_api1.business.example.com");
        sdkConfig.put("acct1.Password", "ZNKHJBXNVL9C4NXJ");
        sdkConfig.put("acct1.Signature", "ApJTjs9JGbPMPRqOqsMiJNhM6bB8AOAQN5WWBLdWWes8dpcLNKKhT55q");

        urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType pprequest = new urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType();
        pprequest.setVersion("63.0");

        SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();

        PaymentDetailsType paymentDetails = new PaymentDetailsType();
        paymentDetails.setOrderDescription("Integrating Stuff Test Order");
        paymentDetails.setInvoiceID("INVOICE-" + Math.random());

        paymentDetails.setPaymentRequestID("PAYREQ-ID-" + Math.random());

        BasicAmountType orderTotal = new BasicAmountType();

        orderTotal.setCurrencyID(currencyCodeType);
        orderTotal.setValue(paymentAmount);
        paymentDetails.setOrderTotal(orderTotal);
        paymentDetails.setPaymentAction(paymentAction);
        pda = Arrays.asList(new PaymentDetailsType[] { paymentDetails });
        details.setPaymentDetails(Arrays.asList(new PaymentDetailsType[] { paymentDetails }));

        details.setReturnURL(returnURL);
        details.setCancelURL(cancelURL);
        details.setCustom(payerId.toString());

        pprequest.setSetExpressCheckoutRequestDetails(details);

        final SetExpressCheckoutReq request = new SetExpressCheckoutReq();
        request.setSetExpressCheckoutRequest(pprequest);
        try {
            SetExpressCheckoutResponseType ppresponse = (SetExpressCheckoutResponseType) service.setExpressCheckout(request);

            System.out.println(ppresponse.getToken());

            return ppresponse.getToken();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "failed";
        }
    }

    public GetExpressCheckoutDetailsResponseDetailsType getExpressCheckoutDetails(String token)
        throws PayPalException, SSLConfigurationException, InvalidCredentialException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, IOException, ParserConfigurationException, SAXException {
        GetExpressCheckoutDetailsRequestType pprequest = new GetExpressCheckoutDetailsRequestType();
        pprequest.setVersion("63.0");
        pprequest.setToken(token);

        final GetExpressCheckoutDetailsReq grequest = new GetExpressCheckoutDetailsReq();

        grequest.setGetExpressCheckoutDetailsRequest(pprequest);

        GetExpressCheckoutDetailsResponseType ppresponse = new GetExpressCheckoutDetailsResponseType();
        try {
            ppresponse = (GetExpressCheckoutDetailsResponseType) service.getExpressCheckoutDetails(grequest);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        GetExpressCheckoutDetailsResponseDetailsType res = ppresponse.getGetExpressCheckoutDetailsResponseDetails();
        PayerInfoType payerInfo = res.getPayerInfo();

        payerInfo.setPayerID("EPZU3PLHGYB8G");
        payerInfo.setContactPhone("0964106670");

        tokn = res.getToken();
        return res;
    }

    public String retGetTkn() {
        return tokn;
    }

    public Boolean doExpressCheckoutService(GetExpressCheckoutDetailsResponseDetailsType response)
        throws PayPalException, SSLConfigurationException, InvalidCredentialException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, IOException, ParserConfigurationException, SAXException {
        final PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(serviceConfigurationProperties);

        DoExpressCheckoutPaymentRequestType pprequest = new DoExpressCheckoutPaymentRequestType();
        pprequest.setVersion("63.0");

        DoExpressCheckoutPaymentResponseType ppresponse = new DoExpressCheckoutPaymentResponseType();

        DoExpressCheckoutPaymentRequestDetailsType paymentDetailsRequestType = new DoExpressCheckoutPaymentRequestDetailsType();
        paymentDetailsRequestType.setToken(response.getToken());
        PayerInfoType payerInfo = response.getPayerInfo();

        paymentDetailsRequestType.setPayerID(payerInfo.getPayerID());
        paymentDetailsRequestType.setPaymentAction(this.paymentAction);

        paymentDetailsRequestType.setPaymentDetails(response.getPaymentDetails());

        pprequest.setDoExpressCheckoutPaymentRequestDetails(paymentDetailsRequestType);
        final DoExpressCheckoutPaymentReq drequest = new DoExpressCheckoutPaymentReq();
        drequest.setDoExpressCheckoutPaymentRequest(pprequest);
        try {
            ppresponse = (DoExpressCheckoutPaymentResponseType) service.doExpressCheckoutPayment(drequest);
            DoExpressCheckoutPaymentResponseDetailsType type = ppresponse.getDoExpressCheckoutPaymentResponseDetails();

            if (type != null) {
                PaymentInfoType paymentInfo = (PaymentInfoType) type.getPaymentInfo();
                if (paymentInfo.getPaymentStatus().equals(PaymentStatusCodeType.valueOf("Completed"))) {
                    log.info("Payment completed.");
                    return true;
                } else {
                    log.info("Payment not completed.. (" + paymentInfo.getPaymentStatus() + ")");
                    return false;
                }
            } else {
                log.info(
                    "Problem executing DoExpressCheckoutPayment.. Maybe you tried to process an ExpressCheckout that has already been processed."
                );
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
