import java.util.*;

/**
 * Created by li-wei on 2015/4/27.
 */
public class Test2 {
    static class TimeSegment {
        double start, end;
        boolean isWeatherInfl;
        public TimeSegment(double start, double end, boolean isWeatherInfl) {
            this.start = start;
            this.end = end;
            this.isWeatherInfl = isWeatherInfl;
        }
    }

    static class QueryCondition {
        double x, y, time;
        boolean badWeather;
        public QueryCondition(double x, double y, double time, boolean badWeather) {
            this.x = x;
            this.y = y;
            this.time = time;
            this.badWeather = badWeather;
        }
    }

    static class Position {
        double x, y;
        String name;
        Set<TimeSegment> times;
        public Position(double x, double y, String name, Set<TimeSegment> times) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.times = times;
        }

        @Override
        public String toString() {
            return "[" + name + ",(" + x + "," + y + ")]";
        }
    }

    static class WeightedPostion implements Comparable<WeightedPostion> {
        Position position;
        double weight;

        public WeightedPostion(Position position, double weight) {
            this.position = position;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "{地点 : " +
                    "" + position +
                    ", 距离 : " + weight +
                    '}';
        }

        public int compareTo(WeightedPostion that) {
            return (int) (this.weight - that.weight);
        }
    }

    private static HashSet<TimeSegment> help(TimeSegment... times) {
        HashSet<TimeSegment> set = new HashSet<>();
        for(TimeSegment time : times)
            set.add(time);
        return set;
    }

    public static void main(String[] args) {
        List<Position> poss = new ArrayList<>();
        poss.add(new Position(0, 0, "F", help(new TimeSegment(0, 0, false))));
        poss.add(new Position(2, 1, "A", help(new TimeSegment(12, 13, false))));
        poss.add(new Position(2, 2, "B", help(new TimeSegment(1, 2, false), new TimeSegment(4,5, true))));
        poss.add(new Position(2, 2, "C", help(new TimeSegment(0, 0, true))));
        Scanner sc = new Scanner(System.in);
        QueryCondition oldPos = null;
        boolean right = false, up = false, left = false, down = false;
        while (true) {
            if(oldPos == null)
                oldPos = parse(sc.next());
            QueryCondition newPos = parse(sc.next());
            double x1 = oldPos.x, x2 = newPos.x, y1 = oldPos.y, y2 = newPos.y;
            if (x2 > x1) if(left) {System.out.println("往右折头"); System.exit(0);} else right = true;
            if (y2 > y1) if(down) {System.out.println("往上折头"); System.exit(0);} else  up = true;
            if (x2 < x1) if(right) {System.out.println("网左折头");  System.exit(0);} else left = true;
            if (y2 < y1) if(up) {System.out.println("往下折头");  System.exit(0);} else  down = true;
            TreeMap<Double,List<WeightedPostion>> map = new TreeMap<>();
            out:
            for (Position p : poss) {
                boolean isTime = false;
                for(TimeSegment time : p.times) {
                    if (time.isWeatherInfl && newPos.badWeather) {
                        continue out;
                    }
                    if (newPos.time >= time.start && newPos.time <= time.end) {
                        isTime = true;
                        break;
                    }
                }
                if (!isTime) continue;
                if (x1 != x2 || y1 != y2) {
                    if (right && p.x < x2)
                        continue;
                    if (left && p.x > x2)
                        continue;
                    if (up && p.y < y2)
                        continue;
                    if (down && p.y > y2)
                        continue;
                } else {
                    System.out.println("输入坐标并未移动，无法判断");
                    break;
                }
                double weight = Math.abs(p.x - x2) + Math.abs(p.y - y2);
                List<WeightedPostion> tmp = map.get(weight);
                if (tmp == null) {
                    tmp = new ArrayList<>();
                    map.put(weight, tmp);
                }
                tmp.add(new WeightedPostion(p, weight));
            }
            if(map.size() == 0)
                    System.out.println("无处可去");
            else {
                List<String> help = new ArrayList<>();
                System.out.println("从起点出发，依次可达: ");
                for(double w : map.keySet())
                    for (WeightedPostion wp : map.get(w)) {
                        if(help.contains(wp.position.name)) {}
                        else {
                            help.add(wp.position.name);
                            System.out.println(wp);
                        }
                    }
                oldPos = newPos;
            }
        }
    }

    public static QueryCondition parse(String input) {
        if(input.equals("end")) System.exit(0);
        String[] temp = input.split(",");
        return new QueryCondition(Double.parseDouble(temp[0].substring(1).trim()),
                Double.parseDouble(temp[1].substring(0, temp[1].length()).trim()),
                Double.parseDouble(temp[2].substring(0, temp[2].length())),
                Boolean.parseBoolean(temp[3].substring(0, temp[3].length() - 1)));
    }

}
