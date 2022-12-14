package com.consultacns;

import ihe.iti.pdqv3._2007.PDQSupplierPortType;
import ihe.iti.pdqv3._2007.PDQSupplierService;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT030007UVPerson;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EN;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.IVLTS;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PDQObjectFactory;
import org.hl7.v3.PN;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02MothersMaidenName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.ST;
import org.hl7.v3.TS;
import org.hl7.v3.XActMoodIntentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.consultacns.model.ConsultaCNS;

@Controller
public class PDQClient {
	
	private static final PDQSupplierService SERVICE;

	static {

		// CONFIGURA A JVM PARA IGNORAR O CERTIFICADOS INVALIDOS
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e1) {
			throw new RuntimeException(e1);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		SERVICE = new PDQSupplierService();

		// Handlers para acionar os parametros de autenticacao no cabecalho da
		// mensagem
		final WSSUsernameTokenSecurityHandler handler = new WSSUsernameTokenSecurityHandler("02432879414", "i1o05sHopx91*");
		SERVICE.setHandlerResolver(new HandlerResolver() {

			@Override
			@SuppressWarnings("rawtypes")
			public List<Handler> getHandlerChain(PortInfo arg0) {
				List<Handler> handlerList = new ArrayList<Handler>();
				handlerList.add(handler);
				return handlerList;
			}
		});
	}

	//private String filtros;
	//private String tipos;
	
	public PDQClient() {
		//this.filtros = filtro;
		//this.tipos = tipo;
	}

	//public static void main(String args[]) {
	//	new PDQClientExample("EDILSON DE OLIVEIRA NOGUEIRA", "Nome").callService();
	//}

	public void callService( String filtro, String tipo ) {
		PDQSupplierPortType pdq = SERVICE.getPDQSupplierPortSoap12();

		PRPAIN201305UV02 body = new PRPAIN201305UV02();
		body.setITSVersion("XML_1.0");
		// Parte fixa
		// <id root="2.16.840.1.113883.4.714" extension="123456"/>
		II ii = new II();
		ii.setRoot("2.16.840.1.113883.4.714");
		ii.setExtension("123456");
		body.setId(ii);
		// <creationTime value="20070428150301"/>
		TS ts = new TS();
		ts.setValue(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		body.setCreationTime(ts);
		// <interactionId root="2.16.840.1.113883.1.6"
		// extension="PRPA_IN201305UV02"/>
		II intId = new II();
		intId.setRoot("2.16.840.1.113883.1.6");
		intId.setExtension("PRPA_IN201305UV02");
		body.setInteractionId(intId);
		// <processingCode code="T"/>
		CS processingCode = new CS();
		processingCode.setCode("T");
		body.setProcessingCode(processingCode);
		// <processingModeCode code="T"/>
		body.setProcessingModeCode(processingCode);
		// <acceptAckCode code="AL"/>
		CS acceptAckCode = new CS();
		acceptAckCode.setCode("AL");
		body.setAcceptAckCode(acceptAckCode);
		// <receiver typeCode="RCV">
		MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
		receiver.setTypeCode(CommunicationFunctionType.RCV);
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceReceiver = new MCCIMT000100UV01Device();
		deviceReceiver.setClassCode(EntityClassDevice.DEV);
		deviceReceiver.setDeterminerCode("INSTANCE");
		// <id root="2.16.840.1.113883.3.72.6.5.100.85"/>
		II idDeviceReceiver = new II();
		idDeviceReceiver.setRoot("2.16.840.1.113883.3.72.6.5.100.85");
		deviceReceiver.getId().add(idDeviceReceiver);
		// </device>
		receiver.setDevice(deviceReceiver);
		body.getReceiver().add(receiver);
		// </receiver>
		// <sender typeCode="SND">
		MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
		sender.setTypeCode(CommunicationFunctionType.SND);
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceSender = new MCCIMT000100UV01Device();
		deviceSender.setClassCode(EntityClassDevice.DEV);
		deviceSender.setDeterminerCode("INSTANCE");
		// <id root="2.16.840.1.113883.3.72.6.2"/>
		II idDeviceSender = new II();
		idDeviceSender.setRoot("2.16.840.1.113883.3.72.6.2");
		deviceSender.getId().add(idDeviceSender);
		// <name>SIGA-SAUDE</name>
		EN nameSender = new EN();
		nameSender.getContent().add("SIGA");
		deviceSender.getName().add(nameSender);
		// </device>
		sender.setDevice(deviceSender);
		body.setSender(sender);
		// </sender>

		// <controlActProcess classCode="CACT" moodCode="EVN">
		PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlAct = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
		controlAct.setClassCode(ActClassControlAct.CACT);
		controlAct.setMoodCode(XActMoodIntentEvent.EVN);
		body.setControlActProcess(controlAct);
		// <code code="PRPA_TE201305UV02" codeSystem="2.16.840.1.113883.1.6"/>
		CD code = new CD();
		code.setCode("PRPA_TE201305UV02");
		code.setCodeSystem("2.16.840.1.113883.1.6");
		controlAct.setCode(code);
		// <queryByParameter>
		PRPAMT201306UV02QueryByParameter queryByParamenter = new PRPAMT201306UV02QueryByParameter();
		// <queryId root="1.2.840.114350.1.13.28.1.18.5.999"
		// extension="1840997084" />
		II queryId = new II();
		queryId.setRoot("1.2.840.114350.1.13.28.1.18.5.999");
		queryId.setExtension("1840997084");
		queryByParamenter.setQueryId(queryId);
		// <statusCode code="new"/>
		CS statusCode = new CS();
		statusCode.setCode("new");
		queryByParamenter.setStatusCode(statusCode);
		// <responseModalityCode code="R"/>
		CS responseModalityCode = new CS();
		responseModalityCode.setCode("R");
		queryByParamenter.setResponseModalityCode(responseModalityCode);
		// <responsePriorityCode code="I"/>
		CS responsePriorityCode = new CS();
		responsePriorityCode.setCode("I");
		queryByParamenter.setResponsePriorityCode(responsePriorityCode);
		// <parameterList>
		PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();
		queryByParamenter.setParameterList(parameterList);
		
		if ("CNS".equals(tipo)) {
			// <livingSubjectId> CNS
			PRPAMT201306UV02LivingSubjectId livingSubjectId = new PRPAMT201306UV02LivingSubjectId();
			parameterList.getLivingSubjectId().add(livingSubjectId);
			// <value root="2.16.840.1.113883.13.236" extension="708006399875323"/>
			// - PESQUISA POR CNS
			II cnsParameter = new II();
			cnsParameter.setRoot("2.16.840.1.113883.13.236");
			cnsParameter.setExtension(filtro.toString());
			livingSubjectId.getValue().add(cnsParameter);
			// <semanticsText>LivingSubject.id</semanticsText>
			ST cnsSemanticsText = new ST();
			cnsSemanticsText.getContent().add("LivingSubject.id");
			livingSubjectId.setSemanticsText(cnsSemanticsText);
			// </livingSubjectId>
		}
		
		if ("CPF".equals(tipo)) {
			//CPF
			PRPAMT201306UV02LivingSubjectId livingSubjectIdCPF = new PRPAMT201306UV02LivingSubjectId();
			parameterList.getLivingSubjectId().add(livingSubjectIdCPF);
			// <value root="2.16.840.1.113883.13.236" extension="708006399875323"/>
			// - PESQUISA POR CNS
			II cpfParameter = new II();
			cpfParameter.setRoot("2.16.840.1.113883.13.237");
			cpfParameter.setExtension(filtro);
			livingSubjectIdCPF.getValue().add(cpfParameter);
			// <semanticsText>LivingSubject.id</semanticsText>
			ST cpfSemanticsText = new ST();
			cpfSemanticsText.getContent().add("LivingSubject.id");
			livingSubjectIdCPF.setSemanticsText(cpfSemanticsText);
		}
		
		if ("Nome".equals(tipo)) {
			//Nome
			PDQObjectFactory factory = new PDQObjectFactory();
			PRPAMT201306UV02LivingSubjectName livingSubjectNome = new PRPAMT201306UV02LivingSubjectName();
			parameterList.getLivingSubjectName().add(livingSubjectNome);
			EN nomeParametro = new EN();
			nomeParametro.getUse().add("L");
			EnGiven given = new EnGiven();
			given.getContent().add(filtro.toString());
			nomeParametro.getContent().add(factory.createENGiven(given));
			livingSubjectNome.getValue().add(nomeParametro);
			ST nomeSemanticsText = new ST();
			nomeSemanticsText.getContent().add("LivingSubject.Given");
			livingSubjectNome.setSemanticsText(nomeSemanticsText);
		}
		
		
		//Sexo
//		PRPAMT201306UV02LivingSubjectAdministrativeGender livingSubjectGender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
//		parameterList.getLivingSubjectAdministrativeGender().add(livingSubjectGender);
//		CE sexoParametro = new CE();
//		sexoParametro.setCodeSystem("2.16.840.1.113883.5.1");
//		sexoParametro.setCode("[SEXO]");
//		livingSubjectGender.getValue().add(sexoParametro);
//		ST sexoSemanticsText = new ST();
//		sexoSemanticsText.getContent().add("LivingSubject.administrativeGender");
//		livingSubjectGender.setSemanticsText(sexoSemanticsText);
		
		
		//LocalID
//		PRPAMT201306UV02LivingSubjectId livingSubjectLocalID = new PRPAMT201306UV02LivingSubjectId();
//		parameterList.getLivingSubjectId().add(livingSubjectLocalID);
//		II localIDParametro = new II();
//		localIDParametro.setRoot("2.16.840.1.113883.3.4594.100.3");
//		localIDParametro.setExtension("[Local ID]");
//		livingSubjectLocalID.getValue().add(localIDParametro);
//		ST localIDSemanticsText = new ST();
//		localIDSemanticsText.getContent().add("LivingSubject.id");
//		livingSubjectLocalID.setSemanticsText(localIDSemanticsText);
		
		//Nome da mae
//		PRPAMT201306UV02MothersMaidenName mothersMaidenName = new PRPAMT201306UV02MothersMaidenName();
//		parameterList.getMothersMaidenName().add(mothersMaidenName);
//		PN nomeMaeParametro = new PN();
//		nomeMaeParametro.getUse().add("L");
//		nomeMaeParametro.getContent().add("MARLENE DE OLIVEIRA");
//		mothersMaidenName.getValue().add(nomeMaeParametro);
//		ST nomeMaeSemanticsText = new ST();
//		nomeMaeSemanticsText.getContent().add("mothersMaidenName");
//		mothersMaidenName.setSemanticsText(nomeMaeSemanticsText);
		
		//Data de Nascimento
//		PRPAMT201306UV02LivingSubjectBirthTime livingSubjectBirhtTime = new PRPAMT201306UV02LivingSubjectBirthTime();
//		parameterList.getLivingSubjectBirthTime().add(livingSubjectBirhtTime);
//		IVLTS birthTime = new IVLTS();
//		birthTime.setValue("19720918000000");
//		livingSubjectBirhtTime.getValue().add(birthTime);
//		ST dataNascimentoST = new ST();
//		dataNascimentoST.getContent().add("LivingSubject.birthTime");
//		livingSubjectBirhtTime.setSemanticsText(dataNascimentoST);
		
		
		// </parameterList>
		// </queryByParameter>
		JAXBElement<PRPAMT201306UV02QueryByParameter> jaxbQuery = new PDQObjectFactory()
				.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryByParamenter);
		controlAct.setQueryByParameter(jaxbQuery);
		// </controlActProcess>

	//	try {
			PRPAIN201306UV02 retorno = pdq.pdqSupplierPRPAIN201305UV02(body);	
			
			System.out.println(retorno.getControlActProcess().getSubject().size());
			
			for (int i = 0; i < retorno.getControlActProcess().getSubject().size(); i++) {
				PRPAMT201310UV02Person patientPerson = retorno.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient()
						.getPatientPerson().getValue();
				System.out.println(patientPerson);
				
				ConsultaCNS consulta = new ConsultaCNS();
				
				/*@SuppressWarnings("unchecked")
				JAXBElement<EnGiven> objNome = (JAXBElement<EnGiven>) patientPerson.getName().get(0).getContent().get(0);

				System.out.println("NOME: " + objNome.getValue().getContent().get(0));
				consulta.setNome(objNome.getValue().getContent().get(0).toString());*/
				
				//IDs
				for (PRPAMT201310UV02OtherIDs otherId : patientPerson.getAsOtherIDs()) {
					for (II id : otherId.getId()) {
						//CNS
						if ("2.16.840.1.113883.13.236".equals(id.getRoot())) {
							System.out.print("CNS: " + id.getExtension() + " ");
							consulta.setCns(id.getExtension());
						}
						if ("2.16.840.1.113883.13.236.1".equals(id.getRoot())) {
							System.out.println("Tipo: " + id.getExtension());
							consulta.setTipo(id.getExtension());
						}				
					}
					@SuppressWarnings("unchecked")
					JAXBElement<EnGiven> objNome = (JAXBElement<EnGiven>) patientPerson.getName().get(0).getContent().get(0);

					System.out.println("NOME: " + objNome.getValue().getContent().get(0));
					consulta.setNome(objNome.getValue().getContent().get(0).toString());
					
					//Nome da mae
					JAXBElement<EnGiven> objNomeMae = (JAXBElement<EnGiven>) patientPerson.getPersonalRelationship().get(0).getRelationshipHolder1().getValue().getName().get(0).getContent().get(0);
					System.out.println("Nome da mae: " + objNomeMae.getValue().getContent().get(0));
					consulta.setMae(objNomeMae.getValue().getContent().get(0).toString());
					//Nome do Pai
					JAXBElement<EnGiven> objNomePai = (JAXBElement<EnGiven>) patientPerson.getPersonalRelationship().get(1).getRelationshipHolder1().getValue().getName().get(0).getContent().get(0);
					System.out.println("Nome do pai: " + objNomePai.getValue().getContent().get(0));
					consulta.setPai(objNomePai.getValue().getContent().get(0).toString());
					//Sexo
					System.out.println("Sexo: " + patientPerson.getAdministrativeGenderCode().getCode());
					consulta.setSexo(patientPerson.getAdministrativeGenderCode().getCode());
					//Data de nascimento
					System.out.println("Data de nascimento: " + patientPerson.getBirthTime().getValue());
					consulta.setDtNasc(patientPerson.getBirthTime().getValue());
					
				}
			}
			
		/*} catch (com.sun.xml.internal.ws.fault.ServerSOAPFaultException e) {
			System.out.println("Falha ao Acessar o Servidor");
		}*/
		

		
	}
}
