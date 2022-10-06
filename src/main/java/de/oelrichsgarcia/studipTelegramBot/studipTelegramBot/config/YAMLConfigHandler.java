package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Yaml config handler.
 */
public class YAMLConfigHandler {

    /**
     * The constant configPath.
     */
    public static final Path configPath = Paths.get("./config.yml");
    private static YAMLConfigHandler yamlConfigHandler;

    /**
     * The Config.
     */
    Config config;

    /**
     * Constructor
     */
    private YAMLConfigHandler(Path configPath) throws IOException {
        this.config = loadConfig(configPath);
    }

    /**
     * Get instance of ConfigHandler
     *
     * @return instance instance
     * @throws IOException the io exception
     */
    public static YAMLConfigHandler getInstance() throws IOException {
        return getInstance(configPath);
    }

    /**
     * Get instance of ConfigHandler
     *
     * @param configPath the config path
     * @return the instance
     * @throws IOException the io exception
     */
    public static YAMLConfigHandler getInstance(Path configPath) throws IOException {
        if (yamlConfigHandler == null) {
            yamlConfigHandler = new YAMLConfigHandler(configPath);
        }
        return yamlConfigHandler;
    }

    /**
     * Create new config.
     *
     * @param config the config
     * @param path   the path
     * @throws IOException the io exception
     */
    public static void createNewConfig(Config config, Path path) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yml = new Yaml(options);
        yml.dump(config, new FileWriter(path.toFile()));
    }

    /**
     * Load config config.
     *
     * @return the config
     * @throws IOException the io exception
     */
    public Config loadConfig() throws IOException {
        return loadConfig(configPath);
    }

    /**
     * Load config.yml
     *
     * @param configPath the config path
     * @return the config
     * @throws IOException the io exception
     */
    public Config loadConfig(Path configPath) throws IOException {
        Constructor constructor = new Constructor(Config.class);
        Yaml yaml = new Yaml(constructor);
        return yaml.load(Files.newInputStream(configPath.toFile().toPath()));
    }

    /**
     * Dump config to config.yml
     *
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IOException              the io exception
     */
    public void dumpConfig() throws IllegalArgumentException, IOException {
        dumpConfig(this.config, configPath);
    }

    /**
     * Dump config to config.yml
     *
     * @param config     the config
     * @param configPath the config path
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IOException              the io exception
     */
    public void dumpConfig(Config config, Path configPath) throws IllegalArgumentException, IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yml = new Yaml(options);
        yml.dump(config, new FileWriter(configPath.toFile()));
    }

    /**
     * Get config object
     *
     * @return the config
     */
    public Config getConfig() {
        return this.config;
    }

}
