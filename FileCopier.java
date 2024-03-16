import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class FileCopier {
    public static void main(String[] args) {
        Path sourceDir = Paths.get("/home/chema/programming");
        Path targetDir = Paths.get("/mnt/c/Users/Chema/Nextcloud/wslProgramming");

        try {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    String dirName = dir.getFileName().toString();
                    if (dirName.equals("node_modules") || dirName.equals("dist")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    Path targetPath = targetDir.resolve(sourceDir.relativize(dir));
                    if (!Files.exists(targetPath)) {
                        Files.createDirectory(targetPath);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                    if (!Files.exists(targetFile)
                            ||
                            Files.getLastModifiedTime(file).compareTo(Files.getLastModifiedTime(targetFile)) > 0) {
                        Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Handle failed file visit
                    return FileVisitResult.CONTINUE;
                }
            });

            // Delete files from target that don't exist in source
            Files.walkFileTree(targetDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path sourceFile = sourceDir.resolve(targetDir.relativize(file));
                    if (!Files.exists(sourceFile)) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Path sourceDirDirectory = sourceDir.resolve(targetDir.relativize(dir));
                    if (!Files.exists(sourceDirDirectory)) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("Files copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}