public class SequenceEntry {

    private int action;
    private String data;
    private long time;

    SequenceEntry(int _action, String _data, long _time) {
        action = _action;
        data = _data;
        time = _time;
    }

    public int getAction() {
        return action;
    }

    public String getData() {
        return data;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.valueOf(action) + ", " +
                data + ", " +
                time;
    }

    public boolean equals(SequenceEntry e) {
        //System.out.println ("Comparing: \n" + toString() + "\n" + e.toString());
        if (getAction() == e.getAction()) {
            if (getData().equals(e.getData())) {
                return Math.round(getTime() / 100.) == Math.round(e.getTime() / 100.);
            }
        }
        return false;
    }

}
