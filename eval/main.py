import numpy as np
import random
import uuid

# Settings
vocab_size = 8000
line_min_words = 8
line_max_words = 12
zipf_param = 2

# Generate vocabulary
vocabulary = [f"word{i}" for i in range(1, vocab_size + 1)]

# Generate Zipf distribution weights for vocabulary
zipf_distribution = np.random.zipf(zipf_param, vocab_size)
# Normalize to ensure the weights sum up to 1
zipf_weights = zipf_distribution / sum(zipf_distribution)


# Generate file content based on Zipf distribution
def generate_file_content(file_size):
    content_size = 0
    content = []

    while content_size < file_size:
        print(f"{content_size}/{file_size}")
        # Generate a line with random number of words between min and max
        line_word_count = random.randint(line_min_words, line_max_words)
        line = ' '.join(random.choices(population=vocabulary, k=line_word_count, weights=zipf_weights))
        content.append(line)
        content_size += len(line) + 1  # Add 1 for the newline character

    return '\n'.join(content)


# Generate files
def create_files(mb: int):
    num_bytes = mb * 1000 * 1000
    file_name = f"{mb}MB_{uuid.uuid1()}.txt"
    print(f"Generating file: {file_name} with size: {mb} MB")
    content = generate_file_content(num_bytes)

    # Write content to file
    with open(file_name, 'w') as f:
        f.write(content)


if __name__ == "__main__":
    create_files(1000)
