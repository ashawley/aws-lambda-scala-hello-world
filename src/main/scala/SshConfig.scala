package prs

case class SshConfig(
  privateKey: String,
  publicKey: String,
  passphrase: String,
  knownHostsFileName: String
)
