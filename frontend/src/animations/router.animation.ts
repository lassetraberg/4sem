import {
  trigger,
  animate,
  transition,
  style,
  query
} from '@angular/animations';

export const routerAnimation = trigger('routerAnimation', [
  // The '* => *' will trigger the animation to change between any two states
  transition('* => *', [
    // The query function has three params.
    // First is the event, so this will apply on entering or when the element is added to the DOM.
    // Second is a list of styles or animations to apply.
    // Third we add a config object with optional set to true, this is to signal
    // angular that the animation may not apply as it may or may not be in the DOM.
    query(
      ':enter',
      [style({ transform: 'translateY(50px)', opacity: 0})],
      { optional: true }
    ),
    query(
      ':leave',
      // here we apply a style and use the animate function to apply the style over 0.3 seconds
      [style({ transform: 'translateY(0)', opacity: 1 }), animate('0.3s cubic-bezier(0.8,0, 0.6,1)', style({transform: 'translateY(50px)', opacity: 0 }))],
      { optional: true }
    ),
    query(
      ':enter',
      [style({ opacity: 0, transform: 'translateY(50px)' }), animate('0.3s cubic-bezier(0.4,0,0.2,1)', style({ opacity: 1, transform: 'translateY(0)' }))],
      { optional: true }
    )
  ])
]);