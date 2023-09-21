resource "google_storage_bucket" "bucket" {
  name     = "tf-test-bucket"
  location = "us-central1"
}