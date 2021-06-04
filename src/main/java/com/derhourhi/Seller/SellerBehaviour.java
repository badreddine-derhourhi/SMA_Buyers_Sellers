package com.derhourhi.Seller;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SellerBehaviour extends CyclicBehaviour{
    private String conversationID;

    public SellerBehaviour(Agent myAgent, String conversationID) {
        super(myAgent);
        this.conversationID=conversationID;
    }


    @Override
    public void action() {
        try {
            MessageTemplate messageTemplate = MessageTemplate.and(
                    MessageTemplate.MatchConversationId(conversationID),
                    MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));

            ACLMessage aclMessage = myAgent.receive(messageTemplate);
            if (aclMessage != null) {

                System.out.println("--------------------------------");
                System.out.println(myAgent.getName()+" ----Buyer accepted proposal...");
                System.out.println(myAgent.getName()+" ----Transaction validation .....");

                ACLMessage replyToComplete = aclMessage.createReply();
                replyToComplete.setPerformative(ACLMessage.CONFIRM);
                Thread.sleep(5000);
                myAgent.send(replyToComplete);
                
                System.out.println(myAgent.getName()+" ----Confirmation sent...");
            } else {
                block();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
