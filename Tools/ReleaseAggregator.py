import os
import shutil
import zipfile


# I know that this code is horrendous, but it works and I can't be bothered to make it better.
# If it ticks you off want to make it better, feel free to do so and submit a pull request.
# I'm pretty tired and I just want to get this done, it's 40ºC with no wind damn it.

# To set this tool up, make a configuration running this on python 3.9 that before running runs
# "mvn clean install package"

def copy_and_zip_files(source_folder: str, target_folder: str) -> None:
    # Create the 'releases' directory if it doesn't exist
    build_dir = os.path.join(target_folder, 'build')
    os.makedirs(build_dir, exist_ok=True)
    auto_version = "0.0.0"

    # Iterate through subdirectories, copy files, and zip them
    for root, _, files in os.walk(source_folder):
        for filename in files:
            if "shaded" in filename:
                parts = filename.split("-")
                file_type = parts[0].split("_")[0]
                version_a = parts[0].split("_")[1]
                version_b = parts[1]

                if auto_version == "0.0.0":
                    auto_version = version_b

                new_filename = f"BountySeekers-{file_type}-{version_a}-{version_b}"
                source_path = os.path.join(root, filename)
                target_path = os.path.join(build_dir, new_filename)
                shutil.copy2(source_path, target_path)
                print(f"Copied and renamed: {source_path} -> {target_path}")

    # Zip the renamed files in the 'releases' directory
    zip_filename = os.path.join(target_folder, 'releases', f'BountySeekers-v{auto_version}.zip')

    with zipfile.ZipFile(os.path.join(target_folder, zip_filename), 'w') as zipf:
        for root, _, files in os.walk(build_dir):
            for filename in files:
                file_path = os.path.join(root, filename)
                print(f"Zipping {file_path}")
                zipf.write(file_path, arcname=os.path.basename(file_path))
                os.remove(file_path)

    shutil.rmtree(build_dir, ignore_errors=True)
    print("Done!")


if __name__ == "__main__":
    source_folder = ".."
    target_folder = "."  # The current directory

    copy_and_zip_files(source_folder, target_folder)
