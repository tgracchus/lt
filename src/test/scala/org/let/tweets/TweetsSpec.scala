package org.let.tweets


import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import org.let.twitter.Tweets
import org.let.twitter.Tweets.{TwitterError, UserTweets, UserTweetsQuery}
import org.let.twitter.twitter4j.TwitterMock
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class TweetsSpec() extends TestKit(ActorSystem("TweetsSpec", ConfigFactory.load("test"))) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with TwitterMock {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An Tweets actor" must {

    "return tweets" in {
      val tweetUser = UserTweetsQuery("user", 10)
      val actorRef = TestActorRef(new Tweets())
      actorRef ! tweetUser
      val result = expectMsgType[UserTweets]
      assert(result.tweets.size == 10)
    }

    "return error" in {
      val tweetUser = UserTweetsQuery("error", 10)
      val actorRef = TestActorRef(new Tweets())
      actorRef ! tweetUser
      val result = expectMsgType[TwitterError]
    }
  }
}
