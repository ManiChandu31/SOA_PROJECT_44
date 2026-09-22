# Local Startup

Run from the TalentSync project root after MySQL databases are available:

```powershell
.\scripts\start-local.ps1 -DbPassword '<your-password>' -JwtSecret '<a shared secret with at least 32 characters>'
```

The script starts Eureka first, waits for it, then starts Auth, Job, Recruitment, Application, and Gateway in separate command windows. It does not write credentials to files or output them.
