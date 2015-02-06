
create schema tweetmood;

create table tweetmood.tweets
(
    tweet_id                auto_increment,
    tweet_request_id        integer     not null,
    tweet_twitter_id        integer     not null,
    tweet_topic             varchar(64),
    tweet_date_created      timestamp,
    tweet_language          varchar(10),
    tweet_text              varchar(200),
    tweet_clean_text        varchar(200),
    tweet_user_id           integer     not null,
    tweet_user_name         varchar(64),
    tweet_retweeted         boolean,
    tweet_favorited         boolean,
    tweet_mood              varchar(10),
    tweet_mood_score        float,
    primary key (tweet_id)
);

create table tweetmood.requests
(
    request_id              integer not null,
    request_created_at      timestamp,
    request_last_completion timestamp,
    request_query           varchar(200),
    request_parent_id       integer     not null,
    request_status          varchar(10),
    request_status_message  varchar(100),
    primary key (request_id)
);

alter table tweetmood.tweets
        add constraint fk_request_id foreign key (tweet_request_id)
            references tweetmood.requests (request_id);
            
    