package com.derhourhi;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class ConsumerAgent extends Agent {

    @Override
    protected void setup() {
        // TODO Auto-generated method stub
        super.setup();
            addBehaviour(new CyclicBehaviour(){

                @Override
                public void action() {
                    // TODO Auto-generated method stub
                        System.out.println("This action is executed multiple times.");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }
                
            });
    }

    @Override
    protected void beforeMove() {
        System.out.println("This is being called before my move to a new container");
    }

    @Override
    protected void afterMove() {
        System.out.println("I'm executing this after moving to my new container");
    }

    @Override
    protected void takeDown() {
        System.out.println("Good bye! i'm going dark");
    }
    
}
