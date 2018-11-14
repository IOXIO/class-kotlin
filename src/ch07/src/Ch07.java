import org.jetbrains.annotations.NotNull;

public class Ch07 {
    public int x = 0;

    public static void main(String[] args) {
        Ch07 ch07 = new Ch07(0);
        //ch07.operatorAndJava();        // 연산자 함수와 자바
        ch07.ch0708();
    }

    public void ch0708() {
        Peaple p1 = new Peaple("Alice", "Smith");
        Peaple p2 = new Peaple("Bob", "Johnson");
        System.out.println(p1.compareTo(p2));
        /*** output ***/
        //-1
        System.out.println(p2.compareTo(p1));
        /*** output ***/
        //1

        Person pe1 = new Person("Alice", "Smith");
        Person pe2 = new Person("Bob", "Johnson");
        System.out.println(p1.compareTo(p2));
        /*** output ***/
        //-1
        System.out.println(p2.compareTo(p1));
        /*** output ***/
        //1
    }

    class Peaple implements Comparable<Peaple> {
        private String lastName;
        private String firstName;
        public Peaple(String lastName, String firstName) {
            this.lastName = lastName;
            this.firstName = firstName;
        }
        @Override
        public int compareTo(@NotNull Peaple other) {
            return lastName.compareTo(other.lastName);
        }
    }

    public void operatorAndJava() {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(30, 30);
        System.out.println("java : " + p1.plus(p2));
        /*** output ***/
        //java : Point(x=40, y=50)
    }

    public Ch07(int x) {
        this.x = x;
    }
}
