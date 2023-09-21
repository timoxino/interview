data "google_project" "project" {
}

resource "google_service_account" "sa_cloud_run" {
  account_id   = "cloud-run-pubsub-invoker"
  display_name = "Cloud Run Pub/Sub Invoker"
}

resource "google_project_service_identity" "pubsub_agent" {
  provider = google-beta
  project  = data.google_project.project.project_id
  service  = "pubsub.googleapis.com"
}

resource "google_project_iam_binding" "project_token_creator" {
  project = data.google_project.project.project_id
  role    = "roles/iam.serviceAccountTokenCreator"
  members = ["serviceAccount:${google_project_service_identity.pubsub_agent.email}"]
}