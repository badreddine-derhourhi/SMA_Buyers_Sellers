package com.derhourhi.Buyer;

import java.util.ArrayList;
import java.util.List;

import com.derhourhi.Other.Book;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.tools.sniffer.Message;

public class RequestBehaviour extends CyclicBehaviour {

    private String conversationID;
    private AID requester;
    private String book;
    private double price;
    private int counter;
    private List<AID> sellers = new ArrayList();
    private int index;
    private double bestPrice;
    private AID bestOffre;

    public RequestBehaviour(Agent myAgent, String book, AID requester, String conversationID) {
        super(myAgent);
        this.book = book;
        this.requester = requester;
        this.conversationID = conversationID;

        System.out.println("Researching services...");
        sellers = searchServices(myAgent, "book-selling");

        System.out.println("Seller list found :");

        for (AID aid : sellers) {
            System.out.println("- " + aid.getName());
        }
        try {
            System.out.println("Sending request for "+book);
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.setContent(book);
            msg.setConversationId(conversationID);
            msg.addUserDefinedParameter("counter", String.valueOf(counter));
            for (AID aid : sellers) {
                msg.addReceiver(aid);
            }
            System.out.println("... waiting for seller");
            Thread.sleep(5000);
            index = 1;
            myAgent.send(msg);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private List<AID> searchServices(Agent myAgent, String type) {
        List<AID> sellers = new ArrayList<>();
        DFAgentDescription agentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(type);
        agentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] descriptions = DFService.search(myAgent, agentDescription);
            for (DFAgentDescription dfAgentDescription : descriptions) {
                sellers.add(dfAgentDescription.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sellers;
    }

    @Override
    public void action() {
        try {
            MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchConversationId(conversationID),
                    MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                            MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)));

            ACLMessage aclMessage = myAgent.receive(template);

            if (aclMessage != null) {
                
                switch (aclMessage.getPerformative()) {
                    case ACLMessage.PROPOSE:
                    
                        Book receivedBook=(Book) aclMessage.getContentObject();
                        if(!receivedBook.isExist())
                            {
                                System.out.println("breaking");
                                break;
                            }
                        
                        System.out.println("Proposal received from: "+aclMessage.getSender());
                        price = receivedBook.getPrice();
                        if (index == 1) {
                            bestPrice = price;
                            bestOffre = aclMessage.getSender();
                        } else {
                            if (price < bestPrice) {
                                bestPrice = price;
                                bestOffre = aclMessage.getSender();
                            }
                        }
                        ++index;

                        if (index == sellers.size()) {
                            System.out.println("Best Price: "+bestPrice);
                            index = 1;
                            ACLMessage aclMessage2 = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                            aclMessage2.addReceiver(bestOffre);
                            aclMessage2.setConversationId(conversationID);
                            Thread.sleep(5000);
                            myAgent.send(aclMessage2);
                            System.out.println("Message sent to "+bestOffre);
                        }
                        
                        break;
                    case ACLMessage.CONFIRM:
                        System.out.println(".............................");
                        System.out.println("Confirmation received for "+book+" ..........");
                        
                        System.out.println("Price :"+bestPrice);
                        
                        System.out.println("Seller :"+aclMessage.getSender().getName());
                        System.out.println("Conversation id: " + aclMessage.getConversationId());
                        ACLMessage aclMessage3 = new ACLMessage(ACLMessage.INFORM);
                        aclMessage3.addReceiver(requester);
                        aclMessage3.setConversationId(conversationID);
                        aclMessage3.setContent("<transaction>" + "<livre>" + book + "</livre>" + "<prix>" + bestPrice
                                + "</prix>" + "<fournisseur>" + aclMessage.getSender().getName() + "</fournisseur>"
                                + "</transaction");
                        myAgent.send(aclMessage3);
                        break;
                }

            } else {
                block();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
