export interface IDashboard {
  id?: number;
}

export class Dashboard implements IDashboard {
  constructor(public id?: number) {}
}

export function getDashboardIdentifier(dashboard: IDashboard): number | undefined {
  return dashboard.id;
}
