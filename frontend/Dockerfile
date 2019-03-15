# Builder
FROM node:alpine AS builder

RUN apk update && apk add --no-cache make git

WORKDIR /build

RUN npm install -g @angular/cli

COPY ./package.json ./package-lock.json ./
RUN npm install

COPY . .

RUN ng build

# Runner
FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /build/dist/frontend /usr/share/nginx/html
COPY --from=builder /build/nginx.conf /etc/nginx/nginx.conf

CMD ["nginx", "-g", "daemon off;"]

