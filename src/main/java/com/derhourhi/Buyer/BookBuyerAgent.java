package com.derhourhi.Buyer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookBuyerAgent extends Agent {

    ParallelBehaviour parallelBehaviour;
    int requestCount;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        String requestedBook="";
        if (args.length >= 1) {
            if (args[0] != null) {
                requestedBook = (String) args[0];
            }else
            {
                System.out.println("No book requested deleting this agent...");
                doDelete();
            }
        }
        final String book=requestedBook;
        System.out.println("I'm the buyer agent for the book "+requestedBook);
        parallelBehaviour=new ParallelBehaviour();
        /*parallelBehaviour.addSubBehaviour(new CyclicBehaviour(){
            @Override
            public void action() {

                MessageTemplate template=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage aclMessage=receive(template);

                if(aclMessage!=null){
                    String book=aclMessage.getContent();
                    AID requester=aclMessage.getSender();
                    ++requestCount;
                    String conversationID = "transaction_" + book + "_" + requestCount;
                    parallelBehaviour.addSubBehaviour(
                            new RequestBehaviour(myAgent, book, requester, conversationID));
                }
                
                
            }
        });*/

        parallelBehaviour.addSubBehaviour(new OneShotBehaviour(){
            @Override
            public void action() {
                ++requestCount;
                    String conversationID = "transaction_" + book + "_" + requestCount;
                    parallelBehaviour.addSubBehaviour(
                            new RequestBehaviour(myAgent, book, this.getAgent().getAID(), conversationID));
            }
        });
        addBehaviour(parallelBehaviour);
    }

}
