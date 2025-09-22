import random

# Word pools
fruits = ["apple", "banana", "cherry", "date", "fig", "grape", "kiwi", "lemon", "mango", "orange", "papaya", "raspberry"]
animals = ["cat", "dog", "elephant", "fox", "giraffe", "horse", "iguana", "jaguar", "koala", "lion", "monkey", "newt"]
colors = ["red", "blue", "green", "yellow", "purple", "pink", "black", "white", "gray", "brown", "orange"]
programming = ["java", "python", "ruby", "csharp", "go", "rust", "typescript", "html", "css", "sql"]
misc = ["sun", "moon", "star", "rocket", "planet", "galaxy", "swim", "fly", "run", "jump", "sit", "stand"]

# Combine and shuffle
word_pool = list(set(fruits + animals + colors + programming + misc))
random.shuffle(word_pool)

def generate_dataset(doc_count, words_per_doc, filename):
    with open(filename, 'w') as f:
        for i in range(1, doc_count + 1):
            doc_id = f"Document{i}"
            words = random.choices(word_pool, k=words_per_doc)
            f.write(f"{doc_id} {' '.join(words)}\n")

# Generate datasets
generate_dataset(10, 100, "dataset_1000.txt")    # 10 docs × 100 words = 1000 words
generate_dataset(30, 100, "dataset_3000.txt")    # 30 docs × 100 words = 3000 words
generate_dataset(50, 100, "dataset_5000.txt")    # 50 docs × 100 words = 5000 words

print("Datasets generated successfully.")

