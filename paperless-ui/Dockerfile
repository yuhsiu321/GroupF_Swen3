FROM nginx:1.20.1
COPY ui/ /usr/share/nginx/html
COPY ui/nginx.conf /etc/nginx/nginx.conf
CMD ["nginx", "-g", "daemon off;"]