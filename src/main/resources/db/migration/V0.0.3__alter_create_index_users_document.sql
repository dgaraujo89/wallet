create unique index users_document on users using btree (document)
    where document is not null;