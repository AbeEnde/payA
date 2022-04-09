package com.mycompany.myapp.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingenico.connect.gateway.sdk.java.Client;
import com.ingenico.connect.gateway.sdk.java.CommunicatorConfiguration;
import com.ingenico.connect.gateway.sdk.java.Factory;
import com.ingenico.connect.gateway.sdk.java.domain.definitions.Address;
import com.ingenico.connect.gateway.sdk.java.domain.definitions.AmountOfMoney;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutRequest;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.definitions.HostedCheckoutSpecificInput;
import com.ingenico.connect.gateway.sdk.java.domain.payment.definitions.Customer;
import com.ingenico.connect.gateway.sdk.java.domain.payment.definitions.Order;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.sdk.exceptions.PayPalException;
import com.mycompany.myapp.drools.BanckInfo;
import com.mycompany.myapp.drools.MergedApiResult;
import com.mycompany.myapp.drools.PersonalInfo;
import com.mycompany.myapp.drools.WholePay;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.jms.Queue;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import com.mycompany.myapp.domain.Payment;

import com.mycompany.myapp.domain.Consumer;
import com.mycompany.myapp.domain.Pay;
import com.mycompany.myapp.domain.PayPal;
import com.mycompany.myapp.domain.PayPalDetailRes;
import com.mycompany.myapp.domain.PayPalProperty;
import com.mycompany.myapp.domain.Payment;
import com.mycompany.myapp.repository.PaymentRepository;
import com.mycompany.myapp.service.DrlService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.GetExpressCheckoutDetailsResponseDetailsType;
import urn.ebay.apis.eBLBaseComponents.PayerInfoType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

/**
 * REST controller for managing {@link pay.domain.Payment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PaymentResource {

    SetExpressCheckoutResponseType setExpressCheckoutResponse;

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";
    Pay pay;
    Pay payment;
    String payP;

    @Autowired
    Consumer cons;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue PaymentQueue;

    @Autowired
    DrlService drs;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    Boolean flag = true;
    private final PaymentRepository paymentRepository;

    public PaymentResource(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * {@code POST  /payments} : Create a new payment.
     *
     * @param payment the payment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payment, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", payment);
        if (payment.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Payment result = paymentRepository.save(payment);

        return ResponseEntity
            .created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/pay")
    public String mapPay(@Valid @RequestBody String payment) {
        this.payP = payment;
        return "accepted";
    }

    int l = 1;
    List<WholePay> alg = new ArrayList<>();

    @GetMapping("/message")
    public List<WholePay> getGreeting() {
        alg = cons.dequee();

        return alg;
    }

    @PostMapping("/delete")
    public WholePay callDelete(@RequestBody Payment pay) throws URISyntaxException {
        WholePay wp = new WholePay();
        alg = cons.dequee();
        for (int i = 0; i < alg.size(); i++) {
            if (alg.get(i).getCik().equalsIgnoreCase(pay.getCik())) {
                alg.remove(i);
                wp = alg.get(i);

                break;
            }
        }

        return wp;
    }

    @PostMapping("/callSave")
    public WholePay callSave(@RequestBody Payment pay) throws URISyntaxException {
        List<WholePay> al = new ArrayList<>();
        WholePay wp = new WholePay();
        al = cons.dequee();
        for (int i = 0; i < al.size(); i++) {
            if (al.get(i).getCik().equalsIgnoreCase(pay.getCik()) && al.get(i).getStatus().contentEquals("Success")) {
                createPayment(pay);
                wp = al.get(i);

                break;
            }
        }
        return wp;
    }

    MergedApiResult sumers = new MergedApiResult();

    @GetMapping("/merge")
    @SendTo("/PaymentQueue")
    public WholePay returnJson(String res)
        throws JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException, MalformedURLException {
        List<Future<String>> futr = getMergedData(); //dc.getMergedData();
        ObjectMapper mapper = new ObjectMapper();

        String reslt = futr.get(0).get();
        String res2 = futr.get(1).get();

        PersonalInfo map1 = mapper.readValue(reslt, PersonalInfo.class);
        BanckInfo[] binf = mapper.readValue(res2, BanckInfo[].class);
        WholePay wp = null;
        for (int i = 0; i < binf.length; i++) {
            if (binf[i].getCik().equalsIgnoreCase(payment.getCik())) {
                wp =
                    new WholePay(
                        payment.getCik(),
                        payment.getCcc(),
                        payment.getPaymentAmount(),
                        payment.getName(),
                        payment.getEmail(),
                        payment.getPhone(),
                        map1.getCountry(),
                        map1.getNationality(),
                        map1.getAge(),
                        binf[i].getBanckName(),
                        binf[i].getAccNo(),
                        binf[i].getDeposit(),
                        "not changed"
                    );
                jmsTemplate.convertAndSend(PaymentQueue, wp);
            }
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" + binf[0].getCik());

        return wp;
    }

    @Value("${adress}")
    String adress;

    @Value("${banckInfo}")
    String banckInfo;

    public String sendAddres() {
        return adress;
    }

    public String sendBanck() {
        return banckInfo;
    }

    public List<Future<String>> getMergedData()
        throws InterruptedException, JsonProcessingException, ExecutionException, MalformedURLException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        RestTemplate restTemplate = new RestTemplate();
        Callable call1 = new Callable<String>() {
            public String call() throws InterruptedException {
                Thread.sleep(1000);

                return restTemplate.getForObject(sendAddres(), String.class);
            }
        };
        Callable call2 = new Callable<String>() {
            public String call() throws InterruptedException {
                Thread.sleep(1000);

                return restTemplate.getForObject(sendBanck(), String.class);
            }
        };

        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(call1);
        callableTasks.add(call2);
        List<Future<String>> futures = executor.invokeAll(callableTasks);

        return futures;
    }

    @PostMapping("/callService")
    public WholePay callService(@Valid @RequestBody Pay payment) {
        this.payment = payment;
        return null;
    }

    @GetMapping("/getM")
    public List<WholePay> getM() {
        Consumer cons = new Consumer();
        if (this.flag == true) {
            List<WholePay> message = cons.dequee();
            return message;
        } else {
            return null;
        }
    }

    /**
     * {@code PUT  /payments/:id} : Updates an existing payment.
     *
     * @param id the id of the payment to save.
     * @param payment the payment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payment,
     * or with status {@code 400 (Bad Request)} if the payment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payments/{id}")
    public ResponseEntity<Payment> updatePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Payment payment
    ) throws URISyntaxException {
        log.debug("REST request to update Payment : {}, {}", id, payment);
        if (payment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Payment result = paymentRepository.save(payment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, payment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payments/:id} : Partial updates given fields of an existing payment, field will ignore if it is null
     *
     * @param id the id of the payment to save.
     * @param payment the payment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payment,
     * or with status {@code 400 (Bad Request)} if the payment is not valid,
     * or with status {@code 404 (Not Found)} if the payment is not found,
     * or with status {@code 500 (Internal Server Error)} if the payment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Payment> partialUpdatePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Payment payment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Payment partially : {}, {}", id, payment);
        if (payment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Payment> result = paymentRepository
            .findById(payment.getId())
            .map(existingPayment -> {
                if (payment.getCik() != null) {
                    existingPayment.setCik(payment.getCik());
                }
                if (payment.getCcc() != null) {
                    existingPayment.setCcc(payment.getCcc());
                }
                if (payment.getPaymentAmount() != null) {
                    existingPayment.setPaymentAmount(payment.getPaymentAmount());
                }
                if (payment.getName() != null) {
                    existingPayment.setName(payment.getName());
                }
                if (payment.getEmail() != null) {
                    existingPayment.setEmail(payment.getEmail());
                }
                if (payment.getPhone() != null) {
                    existingPayment.setPhone(payment.getPhone());
                }

                return existingPayment;
            })
            .map(paymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, payment.getId().toString())
        );
    }

    /**
     * {@code GET  /payments} : get all the payments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payments in body.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws URISyntaxException
     */

    @GetMapping(value = "/payPalDo", produces = { MediaType.APPLICATION_XML_VALUE })
    public String startDoExpresResponse()
        throws ClientActionRequiredException, SSLConfigurationException, MissingCredentialException, OAuthException, InvalidResponseDataException, InvalidCredentialException, IOException, HttpErrorException, InterruptedException, PayPalException, ParserConfigurationException, SAXException {
        try {
            doExpressResponse(getExpressCheckoutDetails(setExpressCheckoutResponse.getToken()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return "SUCCESS PAYMENT ";
    }

    @GetMapping(value = "/payPalDetail", produces = { MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody String returnDetailPayP()
        throws FileNotFoundException, SSLConfigurationException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, OAuthException, InvalidCredentialException, PayPalException, IOException, ParserConfigurationException, SAXException {
        try {
            GetExpressCheckoutDetailsResponseDetailsType detailP = getExpressCheckoutDetails(returnToken());
            String email = detailP.getBuyerMarketingEmail();
            String status = detailP.getCheckoutStatus();
            PayerInfoType payerInfo = detailP.getPayerInfo();
            String phone = payerInfo.getPayerID();
            String inviceID = detailP.getInvoiceID();
            PayPalDetailRes detres = new PayPalDetailRes(email, phone, status, inviceID);
            ObjectMapper Obj = new ObjectMapper();
            String jsonStr = Obj.writeValueAsString(detres);
            return jsonStr;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Value("${returnURL}")
    String returnURL;

    @Value("${cancelURL}")
    String cancelURL;

    public String returnToken()
        throws SSLConfigurationException, InvalidCredentialException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, IOException, ParserConfigurationException, SAXException {
        PayPal ppl = new PayPal();
        Long PayerID = 5l;
        String amount = "50";
        String returnURL = this.returnURL;
        String cancelURL = this.cancelURL;
        PaymentActionCodeType paymentAction = PaymentActionCodeType.SALE;
        CurrencyCodeType currencyCode = CurrencyCodeType.EUR;

        try {
            String token = ppl.setExpressCheckout(PayerID, amount, currencyCode, returnURL, cancelURL, paymentAction);

            return token;
        } catch (PayPalException e) {
            return null;
        }
    }

    @GetMapping(value = "/payPal", produces = { MediaType.APPLICATION_XML_VALUE })
    public String setExpressCheckout()
        throws PayPalException, ClientActionRequiredException, SSLConfigurationException, MissingCredentialException, InvalidResponseDataException, InvalidCredentialException, IOException, HttpErrorException, InterruptedException, ParserConfigurationException, SAXException {
        Long PayerId = 5l;
        String paymentAmount = "25";
        String returnURL = "http://localhost:9000/confrm";
        String cancelURL = "http://localhost:9000/pay-gov";
        PaymentActionCodeType paymentAction = PaymentActionCodeType.SALE;
        CurrencyCodeType currencyCode = CurrencyCodeType.EUR;
        Map<String, String> configurationMap = PayPalProperty.getAcctAndConfig();
        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configurationMap);

        SetExpressCheckoutRequestType setExpressCheckoutReq = new SetExpressCheckoutRequestType();
        setExpressCheckoutReq.setVersion("63.0");

        SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();

        PaymentDetailsType paymentDetails = new PaymentDetailsType();
        paymentDetails.setOrderDescription("PayGove integration with paypal");
        paymentDetails.setInvoiceID("INVOICE-" + Math.random());
        BasicAmountType orderTotal = new BasicAmountType();
        orderTotal.setValue(paymentAmount);
        orderTotal.setCurrencyID(currencyCode);
        paymentDetails.setOrderTotal(orderTotal);
        paymentDetails.setPaymentAction(paymentAction);
        details.setPaymentDetails(Arrays.asList(new PaymentDetailsType[] { paymentDetails }));
        details.setReturnURL(returnURL);
        details.setCancelURL(cancelURL);
        details.setCustom(PayerId.toString());

        setExpressCheckoutReq.setSetExpressCheckoutRequestDetails(details);

        SetExpressCheckoutReq expressCheckoutReq = new SetExpressCheckoutReq();
        expressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutReq);
        try {
            setExpressCheckoutResponse = service.setExpressCheckout(expressCheckoutReq);
            getExpressCheckoutDetails(setExpressCheckoutResponse.getToken());

            return JSONObject.quote(setExpressCheckoutResponse.getToken());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public GetExpressCheckoutDetailsResponseDetailsType getExpressCheckoutDetails(String token)
        throws PayPalException, FileNotFoundException, SSLConfigurationException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, OAuthException, IOException, InterruptedException, InvalidCredentialException, ParserConfigurationException, SAXException {
        Map<String, String> configurationMap = PayPalProperty.getAcctAndConfig();

        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configurationMap);

        GetExpressCheckoutDetailsReq grequest = new GetExpressCheckoutDetailsReq();
        GetExpressCheckoutDetailsRequestType pprequest = new GetExpressCheckoutDetailsRequestType();
        pprequest.setVersion("63.0");

        grequest.setGetExpressCheckoutDetailsRequest(new GetExpressCheckoutDetailsRequestType(token));
        try {
            GetExpressCheckoutDetailsResponseType ppresponse = service.getExpressCheckoutDetails(grequest);

            ppresponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID();
            ppresponse.getGetExpressCheckoutDetailsResponseDetails().getToken();
            ppresponse.getAck();
            ppresponse.getGetExpressCheckoutDetailsResponseDetails();

            return ppresponse.getGetExpressCheckoutDetailsResponseDetails();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void doExpressResponse(GetExpressCheckoutDetailsResponseDetailsType response)
        throws PayPalException, FileNotFoundException, SSLConfigurationException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, OAuthException, IOException, InterruptedException, InvalidCredentialException, ParserConfigurationException, SAXException {
        DoExpressCheckoutPaymentRequestType pprequest = new DoExpressCheckoutPaymentRequestType();
        pprequest.setVersion("63.0");

        DoExpressCheckoutPaymentRequestDetailsType paymentDetailsRequestType = new DoExpressCheckoutPaymentRequestDetailsType();

        paymentDetailsRequestType.setPaymentDetails(response.getPaymentDetails());
        paymentDetailsRequestType.setToken(response.getToken());

        paymentDetailsRequestType.setPayerID(response.getPayerInfo().getPayerID());
        paymentDetailsRequestType.setPaymentAction(PaymentActionCodeType.SALE);
        pprequest.setDoExpressCheckoutPaymentRequestDetails(paymentDetailsRequestType);

        DoExpressCheckoutPaymentReq payRequest1 = new DoExpressCheckoutPaymentReq();
        payRequest1.setDoExpressCheckoutPaymentRequest(pprequest);
    }

    @GetMapping("/redirect")
    public CreateHostedCheckoutResponse getPostWithCustomHeaders() throws IOException, URISyntaxException {
        Client client = getClient();

        try {
            HostedCheckoutSpecificInput hostedCheckoutSpecificInput = new HostedCheckoutSpecificInput();
            hostedCheckoutSpecificInput.setLocale("en_GB");
            hostedCheckoutSpecificInput.setVariant("100");
            hostedCheckoutSpecificInput.setReturnUrl("http://localhost:9000/confrm");
            hostedCheckoutSpecificInput.setShowResultPage(false);
            AmountOfMoney amountOfMoney = new AmountOfMoney();
            amountOfMoney.setAmount((long) pay.getPaymentAmount());
            amountOfMoney.setCurrencyCode("USD");

            Address billingAddress = new Address();
            billingAddress.setCountryCode("US");

            Customer customer = new Customer();
            customer.setBillingAddress(billingAddress);
            customer.setMerchantCustomerId("1378-1234");

            Order order = new Order();
            order.setAmountOfMoney(amountOfMoney);
            order.setCustomer(customer);

            CreateHostedCheckoutRequest body = new CreateHostedCheckoutRequest();
            body.setHostedCheckoutSpecificInput(hostedCheckoutSpecificInput);
            body.setOrder(order);

            CreateHostedCheckoutResponse response = client.merchant("1378").hostedcheckouts().create(body);

            return response;
        } finally {
            client.close();
        }
    }

    @Value("${spring.application.apiKeyId}")
    String apiKeyId;

    @Value("${spring.application.secretApiKey}")
    String secretApiKey;

    private com.ingenico.connect.gateway.sdk.java.Client getClient() throws URISyntaxException {
        String apiKeyId = System.getProperty("apiKeyId", this.apiKeyId);
        String secretApiKey = System.getProperty("secretApiKey", this.secretApiKey);

        URL propertiesUrl = getClass().getResource("/example-configuration.properties");
        CommunicatorConfiguration configuration = Factory.createConfiguration(propertiesUrl.toURI(), apiKeyId, secretApiKey);
        return Factory.createClient(configuration);
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        log.debug("REST request to get all Payments");
        return paymentRepository.findAll();
    }

    /**
     * {@code GET  /payments/:id} : get the "id" payment.
     *
     * @param id the id of the payment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<Payment> payment = paymentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(payment);
    }

    /**
     * {@code DELETE  /payments/:id} : delete the "id" payment.
     *
     * @param id the id of the payment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.debug("REST request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
