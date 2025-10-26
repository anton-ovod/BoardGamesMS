# Use the official PostgreSQL image from Docker Hub
FROM postgres:16-alpine

# Set environment variables
ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=admin123
ENV POSTGRES_DB=boardgamesdb


#Volume
VOLUME ["/var/lib/postgresql/data"]

# Expose the PostgreSQL port
EXPOSE 5432

# Use the default PostgreSQL entrypoint
CMD ["postgres"]
