package question1;

/**
 * 储存url和url请求的次数。
 */
public class Url implements Comparable<Url>{
    private String url;
    private int total;
    public Url(String url, int total) {
        this.url = url;
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                ", total=" + total +
                '}';
    }

    @Override
    public int compareTo(Url o) {
        return this.total - o.total;
    }
}

