import { useEffect } from "react";

export default function slide(elementRefs: React.MutableRefObject<Array<HTMLElement>>, querySelector: string, direction: Direction) {
    useEffect(() => {
        elementRefs.current = Array.from(document.querySelectorAll(querySelector));

        elementRefs.current.forEach((heading) => {
            const observer = new IntersectionObserver(
                ([entry], observer) => {
                    if (entry.isIntersecting) {
                        entry.target.animate(
                            [
                                {
                                    transform: direction == Direction.Left ? "translateX(-50px)" : "translateX(+50px)",
                                    opacity: 0
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

            observer.observe(heading);
        });
    }, [querySelector, direction]);
}

export enum Direction {
    Left,
    Right,
}
