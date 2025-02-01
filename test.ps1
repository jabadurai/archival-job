# Define the directory path
$inputDir = "C:\codebase\archival-job\process\input"

# Create the directory if it doesn't exist
if (-Not (Test-Path -Path $inputDir)) {
    New-Item -ItemType Directory -Path $inputDir
}

# Define the number of files to create
$numFiles = 10

# Create multiple input files
for ($i = 1; $i -le $numFiles; $i++) {
    $fileName = "file_$(Get-Date -Format 'yyyyMMdd_HH_mmss')_$i.txt"
    $filePath = Join-Path -Path $inputDir -ChildPath $fileName
    Add-Content -Path $filePath -Value "This is the content of file $i"
    Start-Sleep -Milliseconds 1000 # Ensure unique timestamp in filename
}

Write-Host "Created $numFiles files in $inputDir"
