# server.py
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import os
import datetime

class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        # Мінімальне логування
        print(f"[{datetime.datetime.now().isoformat()}] {self.client_address[0]} {self.command} {self.path}")

        if self.path == "/ping":
            self.send_response(200)
            self.send_header("Content-Type", "text/plain; charset=utf-8")
            self.end_headers()
            self.wfile.write(b"pong")
        else:
            self.send_error(404, "Not Found")

    # Вимкнути стандартні HTTPServer-логи
    def log_message(self, format, *args):
        return

if __name__ == "__main__":
    port = int(os.environ.get("PORT", "8080"))
    with ThreadingHTTPServer(("0.0.0.0", port), Handler) as httpd:
        print(f"Server started on port {port}")
        httpd.serve_forever()
