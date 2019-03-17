import {
  trigger,
  animate,
  transition,
  style,
  query
} from '@angular/animations';

export const routerAnimation = trigger('routerAnimation', [
  transition('* => *', [
    query(
      ':enter',
      [style({ transform: 'translateY(50px)', opacity: 0})],
      { optional: true }
    ),
    query(
      ':leave',
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