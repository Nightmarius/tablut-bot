import { useEffect, useRef } from "react";

export enum Direction {
    Left,
    Right,
}

export default function useSlideAnimationOnIntersection(querySelector: string, direction: Direction) {
    const elements = useRef<Array<HTMLElement>>([]);

    useEffect(() => {
        elements.current = Array.from(document.querySelectorAll(querySelector));

        elements.current.forEach((element) => {
            const observer = new IntersectionObserver(
                ([entry], observer) => {
                    if (entry.isIntersecting) {
                        entry.target.animate(
                            [
                                {
                                    transform: direction === Direction.Left ? "translateX(-50px)" : "translateX(+50px)",
                                    opacity: 0,
                                },
                                { transform: "translateX(0)", opacity: 1 },
                            ],
                            {
                                duration: 1500,
                                fill: "forwards",
                            }
                        );

                        // only animate once
                        observer.unobserve(entry.target);
                    }
                },
                {
                    // start animating when 50% of the element is visible
                    threshold: 0.5,
                }
            );

            observer.observe(element);
        });
    }, [querySelector, direction]);
}
