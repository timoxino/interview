resource "google_pubsub_topic" "topic_extracted_skills" {
  name = "extracted_skills_topic"
}

resource "google_pubsub_subscription" "subscription_sage" {
  name  = "sage_subscription"
  topic = google_pubsub_topic.topic_extracted_skills.name
  enable_exactly_once_delivery = true
  ack_deadline_seconds = 600
}
