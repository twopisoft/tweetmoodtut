
create schema tweetmood;

create table tweetmood.tweets
(
    tweet_id                integer     identity(1,1) primary key,
    tweet_request_id        integer     not null,
    tweet_twitter_id        integer     not null,
    tweet_topic             varchar(64),
    tweet_date_created      timestamp,
    tweet_language          varchar(10),
    tweet_text              varchar(200),
    tweet_user_id           integer     not null,
    tweet_user_name         varchar(64),
    tweet_retweeted         boolean,
    tweet_favorited         boolean,
    tweet_mood              varchar(10),
    tweet_mood_score        float
);

create table tweetmood.requests
(
    request_id              integer     identity(1,1) primary key,
    request_created         timestamp,
    request_query           varchar(200),
    request_parent_id       integer     not null,
    request_status          varchar(10)
);

alter table tweetmood.tweets
        add constraint fk_request_id foreign key (request_id)
            references tweetmood.requests (request_id);
            
    