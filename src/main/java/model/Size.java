package model;

/**
 * Enum representing the different sizes available for a product.
 * Typically used for products like drinks, meals, or other items where size variations exist.
 * <p>
 * The available sizes are:
 * <ul>
 *   <li>{@link #BIG} - Large size</li>
 *   <li>{@link #MEDIUM} - Medium size</li>
 *   <li>{@link #SMALL} - Small size</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>
 *     Size size = Size.MEDIUM;
 *     System.out.println("Selected size: " + size);
 * </pre>
 * This enum ensures type safety by limiting the possible values to the predefined constants.
 *
 * @author DaniAndries
 * @version 0.1
 */
public enum Size {
    /** Large size */
    BIG,

    /** Medium size */
    MEDIUM,

    /** Small size */
    SMALL
}
