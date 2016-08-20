package xxx;


@Upcase
@Downcase
public class SimpleClass {
    private String x;

    public SimpleClass(String x) {
        this.x = x;
    }

    @Upcase
    public String upcase() {
        return x;
    }

    @Downcase
    public void downcase(String x) {
        this.x = x;
    }

    public String getAsIs() {
        return x;
    }
}
