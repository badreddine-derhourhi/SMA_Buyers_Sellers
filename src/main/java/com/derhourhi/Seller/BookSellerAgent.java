package com.derhourhi.Seller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.derhourhi.Other.Book;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * BookSellerAgent
 */
public class BookSellerAgent extends Agent {

    private Map<String, Double> data = new HashMap<>();
    private ParallelBehaviour parallelBehaviour;
    private List<Book> books;

    @Override
    protected void setup() {

        Object[] args = getArguments();
        if (args.length >= 1) {
            if (args[0] != null) {
                books = (ArrayList<Book>) args[0];
            }
        }
        System.out.println("Adding service to the Directory Facilitator");
        System.out.println(books);
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(this.getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("book-selling");
        serviceDescription.setName("book-trading");

        agentDescription.addServices(serviceDescription);

        try {
            DFService.register(this, agentDescription);
        } catch (Exception e) {
            e.printStackTrace();
        }

        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                    ACLMessage aclMessage = receive(messageTemplate);
                    if (aclMessage != null) {
                        String bookName = aclMessage.getContent();
                        Book book=new Book();
                        book.setName(bookName);
                        book.setPrice(-1);
                        for (Book b : books) {
                            if (b.getName().equals(bookName)) {
                                book.setPrice(b.getPrice());
                                book.setExist(true);
                                break;
                            }
                        }
                        ACLMessage replyToPropose = aclMessage.createReply();
                        replyToPropose.setPerformative(ACLMessage.PROPOSE);
                        replyToPropose.setContentObject(book);
                        System.out.println(getName() + " ----Sending proposal to buyer.... book: " + book);
                        Thread.sleep(5000);
                        send(replyToPropose);
                        parallelBehaviour.addSubBehaviour(new SellerBehaviour(myAgent, aclMessage.getConversationId()));
                    } else {
                        block();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addBehaviour(parallelBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}