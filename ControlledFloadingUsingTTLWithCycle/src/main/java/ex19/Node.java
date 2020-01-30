package ex19;
import akka.actor.*;
import java.util.*;
import akka.event.*;

public class Node extends UntypedAbstractActor
{
  Node() {neighbours = new ArrayList<ActorRef>();}

  List<ActorRef> neighbours;

  public static Props createActor()
  {
    return Props.create(Node.class,
    () -> {return new Node(); } );
  }

  @Override
  public void onReceive(Object msg)
  {
    if(msg instanceof Map.Entry<?,?>)
    {
      if ( ((Map.Entry<?,?>) msg).getKey() instanceof ActorRef[]
        && ((Map.Entry<?,?>) msg).getValue() instanceof boolean[])
      {
          int i;
          Map.Entry<ActorRef[],boolean[]> p = (Map.Entry<ActorRef[],boolean[]>) msg;
          for (i = 0; i < p.getKey().length; i++)
          {
            if (p.getValue()[i])
            {
              neighbours.add(p.getKey()[i]);
              System.out.println(getSelf().path().name() + ": adding " + i);
            }
          }
        }
      }
      else if(msg instanceof Message)
      {
        if (msg instanceof NullMessage) return;
        System.out.println(getSelf().path().name() + ": received message: " + ((Message) msg).readMessage());

        ListIterator<ActorRef> it = neighbours.listIterator();
        Message toSend = ((Message) msg).getCopy();
        while(it.hasNext())
        {
          it.next().tell(toSend, getSelf());
        }
      }
    }
}
