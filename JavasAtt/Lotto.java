import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;


public class Lotto {
    Toylist currentToys;
    ParticipantPriority currentParticipants;
    double lossWeight = 0;
    int lossId;

    ChanceCalc cc = new ChanceCalc();
    Lotto.QuantityCalc qc = new Lotto.QuantityCalc();

    public Lotto(ParticipantPriority kids, Toylist tl) {

        this.currentToys = cc.assignChance(tl);
        this.currentParticipants = kids;
    }


    public void runRaffle() {
        ParticipantPriority kids = this.currentParticipants;
        Toylist tl = this.currentToys;
        PriorityQueue<Toy> prizes = new PriorityQueue<>(tl.toys.values());
        try {
            BufferedWriter log = FilesWrite.raffleLog();

            while (kids.iterator().hasNext()) {
                double winRoll = cc.doRoll();
                Participant k = kids.iterator().next();
                try {
                    Toy win = cc.checkPrize(prizes, winRoll);
                    prizes = qc.adjustQuantityLeft(win, tl, prizes);
                    log.write(showWin(k, win) + "\n");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            log.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    String showWin(Participant kid, Toy prize) {
        String winLine;
        if (prize.name.equals("ничего")) {
            winLine = kid.toString() + " не выиграл ничего";
        } else {
            winLine = kid.toString() + " выиграл " + prize.name;
        }
        System.out.println(winLine);
        return winLine;
    }

    class QuantityCalc {

        PriorityQueue<Toy> adjustQuantityLeft(Toy t, Toylist tl, PriorityQueue<Toy> currentQueue) {
            if (t.quantity > 0) {
                t.quantity -= 1;
            }
            if (t.quantity == 0) {
                removeStock(t.id, tl);
                Lotto.this.cc.assignChance(tl);
                currentQueue = new PriorityQueue<>(tl.toys.values());
            }
            return currentQueue;
        }

        void removeStock(int idNum, Toylist toys) {
            toys.removeToy(idNum);
        }


    }

}

class ChanceCalc {
    Random r = new Random();
    double maxChance;
    double totalWeight;

    double doRoll() {
        return r.nextDouble() * maxChance;
    }

    Toy checkPrize(PriorityQueue<Toy> prizes, double roll) throws Exception {
        PriorityQueue<Toy> onePoll = new PriorityQueue<>(prizes);

        while (!onePoll.isEmpty()) {
            Toy p = onePoll.poll();
            if (roll <= p.getChance()) {
                return checkTies(onePoll, p);
            }
        }
        throw new Exception("Приз с такой вероятностью не найден");
    }

    Toylist assignChance(Toylist tl) {
        this.totalWeight = 0;
        this.maxChance = 0;
        for (Toy t : tl.toys.values()) {
            this.totalWeight += t.chanceWeight;
        }

        for (Toy t : tl.toys.values()) {
            double ch = t.chanceWeight / totalWeight;
            t.setChance(ch);
            if (maxChance < ch) {
                maxChance = ch;
            }
        }
        return tl;
    }

    Toy checkTies(PriorityQueue<Toy> leftovers, Toy drawn) {
        PriorityQueue<Toy> tiePoll = new PriorityQueue<>(leftovers);
        ArrayList<Toy> sameChance = new ArrayList<>();
        while (!tiePoll.isEmpty()) {
            if (drawn.getChance() == tiePoll.peek().getChance()) {
                sameChance.add(tiePoll.poll());
            } else {
                break;
            }
        }
        sameChance.add(drawn);
        int pickRandom = r.nextInt(sameChance.size());
        return sameChance.get(pickRandom);
    }


}