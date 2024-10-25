import random
import datetime
import string
import json
import sys
import uuid

# Movie-related content
movie_title = ["Inception", "BadBoys", "Gangstar", "BreakingBad", "Matrix",
               "Interstellar", "TheGodFather", "PulpFiction", "FightClub", "TheDarkKnight"]
positive_comments = [
    "Mind-blowing visuals in BadBoys! The way they portrayed dreams was incredible.",
    "The plot twists kept me on the edge of my seat. It was great!",
    "Christopher Nolan outdid himself. it's a masterpiece of modern cinema.",
    "The acting was top-notch, especially Leonardo DiCaprio's performance.",
    "The soundtrack is hauntingly beautiful. it perfectly complements the story."
]
negative_comments = [
    "It was too confusing to follow. I felt lost throughout the movie.",
    "Overrated movie. Didn't live up to the hype. I expected more depth.",
    "The ending left me unsatisfied. I wanted more closure.",
    "Too many plot holes. Made it hard to enjoy the film fully.",
    "Tried too hard to be clever. it felt convoluted at times. It was bad.",
    "The pacing was off, making some scenes drag on unnecessarily. Found it confusing.",
    "The concept was interesting, but execution was poor. I couldn't connect with the characters."
]

def generate_username():
    return ''.join(random.choices(string.ascii_lowercase + string.digits, k=random.randint(5, 15)))

def generate_location():
    locations = ["New York, USA", "Los Angeles, USA", "London, UK", "Tokyo, Japan", "Paris, France", "Berlin, Germany", "Sydney, Australia", "Toronto, Canada", "Mumbai, India", "São Paulo, Brazil"]
    return random.choice(locations)

def generate_hashtags(is_movie: bool):
    if is_movie:
        hashtags = ['#movie', '#film', '#cinema', '#hollywood', '#blockbuster']
    else:
        hashtags = ['#coffee', '#weekend', '#tech', '#food', '#hiking', '#tvshow', '#concert', '#book', '#traffic', '#workout']
    return ','.join(random.sample(hashtags, k=random.randint(1, 4)))

def generate_random_tweet():
    topics = [
        "Just had the best coffee ever! Can't start my day without it.",
        "Can't wait for the weekend! Planning to binge-watch my favorite show.",
        "New tech gadget arrived today. Excited to unbox and try it out!",
        "Trying out a new recipe for dinner tonight. Hope it turns out well!",
        "Weather is perfect for a hike. Nature is calling!",
        "Binge-watching my favorite show. it's so addictive!",
        "Excited about the upcoming concert this weekend! Music is life.",
        "Just finished an amazing book. It changed my perspective on life.",
        "Traffic is terrible today. I might be late for work again!",
        "Loving my new workout routine. Feeling healthier every day!"
    ]
    return random.choice(topics)

def generate_movie_tweet(is_positive):
    if is_positive:
        return (random.choice(positive_comments) +
                f" What did you think of {random.sample(movie_title, k=1)[0]}? #MovieNight")
    else:
        return (random.choice(negative_comments) +
                f" I really wanted to like {random.sample(movie_title, k=1)[0]}, but... #Disappointed")

def generate_tweet():
    username = generate_username()
    location = generate_location()

    # 1% chance of generating a movie-related tweet
    if random.random() < 0.01:
        # 60% negative, 40% positive for movie tweets
        is_positive = random.random() > 0.6
        text = generate_movie_tweet(is_positive)
        hashtag = generate_hashtags(True)
    else:
        text = generate_random_tweet()
        hashtag = generate_hashtags(False)

    timestamp = (datetime.datetime.now() - datetime.timedelta(days=random.randint(0, 365),
                                                              hours=random.randint(0, 23),
                                                              minutes=random.randint(0, 59)))

    tweet = {
        "id": str(uuid.uuid4()),
        "username": username,
        "location": location,
        "text": text,
        "timestamp": timestamp.isoformat(),
        "likes": random.randint(0, 1000),
        "retweets": random.randint(0, 200),
        "replies": random.randint(0, 50),
        "hashtags": hashtag,
        "language": "en",
        "device": random.choice(["Twitter for Android", "Twitter for iPhone", "Twitter Web App"]),
        "is_verified": random.random() < 0.1,
        "follower_count": random.randint(50, 10000),
        "following_count": random.randint(50, 1000),
    }

    return json.dumps(tweet)

def generate_tweet_file(filename, num_tweets):
    with open(filename, 'w', encoding='utf-8') as f:
        for _ in range(num_tweets):
            f.write(generate_tweet() + '\n')


if __name__ == '__main__':
    generate_tweet_file(f'tweets_{sys.argv[1]}.txt', int(sys.argv[1]))
