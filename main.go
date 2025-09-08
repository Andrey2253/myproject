import (
	"fmt"
	"log"
	"net/http"
	"time"
)

func logReq(r *http.Request) {
	ip := r.RemoteAddr
	ts := time.Now().UTC().Format(time.RFC3339)
	log.Printf("[%s] %s %s %s", ts, ip, r.Method, r.URL.Path)
}

func ping(w http.ResponseWriter, r *http.Request) {
	logReq(r)
	if r.URL.Path != "/ping" || r.Method != http.MethodGet {
		http.NotFound(w, r)
		return
	}
	w.Header().Set("Content-Type", "text/plain; charset=utf-8")
	fmt.Fprint(w, "pong")
}

func main() {
	mux := http.NewServeMux()
	mux.HandleFunc("/", ping) // простий роутер

	addr := "0.0.0.0:8080"
	log.Printf("Server GO started on %s", addr)
	if err := http.ListenAndServe(addr, mux); err != nil {
		log.Fatal(err)
	}
}